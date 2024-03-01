package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureIngredient () throws Exception {
        service.deleteAll();

        final Ingredient i = new Ingredient( "Coffee", 1 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {

        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Milk", 1 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddIngredient2 () throws Exception {

        /*
         * Tests a Ingredient with a duplicate name to make sure it's rejected
         */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient( "Coffee", 1 );

        service.save( i1 );

        final Ingredient i2 = new Ingredient( "Coffee", 1 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one Ingredient in the CoffeeMaker" );

    }

    @Test
    @Transactional
    public void testDeleteIngredient () throws Exception {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient r1 = new Ingredient( "Coffee", 1 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(),
                "There should only be one Ingredient in the CoffeeMaker" );

        service.deleteAll();

        final Ingredient r2 = new Ingredient( "Milk", 2 );

        final Ingredient r3 = new Ingredient( "Chocolate", 3 );

        service.save( r3 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.findAll().size(), "There should be 2 Ingredients in the CoffeeMaker" );

        service.deleteAll();
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

    }

    @Test
    @Transactional
    public void testDeleteIngredientAPI () throws Exception {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient r1 = new Ingredient( "Coffee", 1 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(),
                "There should only be one Ingredient in the CoffeeMaker" );

        mvc.perform( delete( "/api/v1/ingredients/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

    }

    /**
     * Tests the getIngredient() method by adding a Ingredient into the system
     * and calling the getIngredient() method to make sure it is in the system.
     * It then adds another Ingredient and calls the getIngredient() method to
     * make sure that Ingredient was also added to the system without error. It
     * then tries to retrieve a Ingredient that was not added to the system to
     * make sure it returns a not found status.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testGetIngredient () throws Exception {
        service.deleteAll();

        final Ingredient Ingredient = new Ingredient( "Coffee", 1 );
        assertEquals( "Coffee", Ingredient.getName() );
        assertEquals( 1, Ingredient.getAmount() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( Ingredient ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        mvc.perform( get( "/api/v1/ingredients/" + Ingredient.getName() ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testGetIngredients () throws Exception {
        service.deleteAll();

        final Ingredient Ingredient = new Ingredient( "Coffee", 1 );
        final Ingredient Ingredient2 = new Ingredient( "Tea", 1 );
        assertEquals( "Coffee", Ingredient.getName() );
        assertEquals( 1, Ingredient.getAmount() );
        assertEquals( "Tea", Ingredient2.getName() );
        assertEquals( 1, Ingredient2.getAmount() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( Ingredient ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( Ingredient2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, (int) service.count() );

        mvc.perform( get( "/api/v1/ingredients" ) ).andExpect( status().isOk() );

    }

}
