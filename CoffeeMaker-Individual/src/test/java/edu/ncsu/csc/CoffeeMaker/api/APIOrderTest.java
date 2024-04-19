package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderService          orderService;

    @Autowired
    private RecipeService         recipeService;

    @Autowired
    private CustomerService       customerService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        orderService.deleteAll();
        recipeService.deleteAll();
        customerService.deleteAll();
    }

    @Test
    @Transactional
    public void testAddOrderAPI () throws Exception {

        orderService.deleteAll();
        recipeService.deleteAll();
        customerService.deleteAll();

        Assertions.assertEquals( 0, (int) orderService.count() );
        Assertions.assertEquals( 0, (int) recipeService.count() );
        Assertions.assertEquals( 0, (int) customerService.count() );

        // create recipe for order
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 1 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 0 ) ) );

        recipeService.save( r1 );

        final Customer customer = new Customer();
        customer.setName( "John" );
        customer.setType( "CUSTOMER" );
        customer.setMoney( 5 );
        customer.setId( 2 );

        customerService.save( customer );

        Assertions.assertEquals( 0, (int) orderService.count() );

        Assertions.assertEquals( 1, (int) customerService.count() );

        mvc.perform( post( "/api/v1/John/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) orderService.count() );

    }

    @Test
    @Transactional
    public void TestPickupOrderAPI () throws Exception {

        orderService.deleteAll();
        recipeService.deleteAll();
        customerService.deleteAll();

        Assertions.assertEquals( 0, (int) orderService.count() );
        Assertions.assertEquals( 0, (int) recipeService.count() );
        Assertions.assertEquals( 0, (int) customerService.count() );

        // create recipe for order
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 1 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 0 ) ) );

        recipeService.save( r1 );

        final Customer customer = new Customer();
        customer.setName( "John" );
        customer.setType( "CUSTOMER" );
        customer.setMoney( 5 );
        customer.setId( 2 );

        customerService.save( customer );

        Assertions.assertEquals( 0, (int) orderService.count() );

        mvc.perform( post( "/api/v1/John/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        final Order order2 = orderService.findAll().get( 0 );

        Assertions.assertEquals( 1, (int) orderService.count() );

        mvc.perform( delete( "/api/v1/orders/" + order2.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 0, (int) orderService.count() );

    }

}
