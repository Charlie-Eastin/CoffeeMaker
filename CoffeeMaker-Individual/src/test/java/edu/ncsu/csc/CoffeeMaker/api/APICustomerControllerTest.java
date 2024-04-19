package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.security.test.context.support.WithMockUser;
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
public class APICustomerControllerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomerService       service;

    @Autowired
    private OrderService          orderService;

    @Autowired
    private RecipeService         recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
        orderService.deleteAll();
        recipeService.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser ( username = "customer", authorities = { "CUSTOMER" } )
    public void ensureIngredient () throws Exception {
        service.deleteAll();
        orderService.deleteAll();
        recipeService.deleteAll();

        final Customer customer = new Customer();
        customer.setName( "John" );
        customer.setMoney( 5 );

        service.save( customer );

        Assertions.assertEquals( 0, (int) orderService.count() );

        final Customer user = service.findByName( "John" );
        Assertions.assertEquals( "John", user.getName() );

        Assertions.assertEquals( 5, service.findByName( "John" ).getMoney() );

        mvc.perform( put( "/api/v1/customers/users/John" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 10 ) ) ).andExpect( status().isOk() );

        mvc.perform( put( "/api/v1/customers/users/Random" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 10 ) ) ).andExpect( status().isConflict() );

        mvc.perform( get( "/api/v1/username" ) ).andReturn().getResponse().getContentAsString().contains( "customer" );

        mvc.perform( get( "/api/v1/role" ) ).andReturn().getResponse().getContentAsString().contains( "CUSTOMER" );

    }

    @Test
    @Transactional
    public void getCustomer () throws Exception {
        service.deleteAll();
        orderService.deleteAll();
        recipeService.deleteAll();

        final Customer customer = new Customer();
        customer.setName( "John" );
        customer.setMoney( 5 );
        customer.setType( "CUSTOMER" );

        // create recipe for order
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 1 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 0 ) ) );

        recipeService.save( r1 );

        service.save( customer );

        final Order order = new Order( r1, customer );

        Assertions.assertEquals( 0, (int) orderService.count() );

        mvc.perform( post( "/api/v1/John/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order ) ) ).andExpect( status().isOk() );

        final Customer user = service.findByName( "John" );
        Assertions.assertEquals( "John", user.getName() );

        Assertions.assertTrue( mvc.perform( get( "/api/v1/customers/users/John" ) ).andReturn().getResponse()
                .getContentAsString().contains( "Coffee" ) );

    }

}
