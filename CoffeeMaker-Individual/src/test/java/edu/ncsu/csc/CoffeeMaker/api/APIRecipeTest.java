package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService         service;

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
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        assertTrue( r.addIngredient( new Ingredient( "Chocolate", 5 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Coffee", 3 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Milk", 4 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Sugar", 8 ) ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        assertTrue( recipe.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r1.setPrice( 5 );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r2.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );

    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r1.setPrice( 5 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r2.setPrice( 5 );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Coffee" );
        assertTrue( r3.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r3.setPrice( 5 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe();
        r4.setName( "Hot Chocolate" );
        assertTrue( r4.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r4.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r4.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r4.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r4.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Insufficient space in recipe book for recipe" );
    }

    @Test
    @Transactional
    public void testDeleteRecipeAPI () throws Exception {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r1.setPrice( 5 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only be one recipe in the CoffeeMaker" );

        service.delete( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Hot Chocolate" );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r2.setPrice( 5 );

        final Recipe r3 = new Recipe();
        r3.setName( "Hot Chocolate" );
        assertTrue( r3.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r3.setPrice( 5 );

        service.save( r3 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.findAll().size(), "There should only be one recipe in the CoffeeMaker" );

        service.deleteAll();
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no recipes in the CoffeeMaker" );

    }

    /**
     * Tests the getRecipe() method by adding a recipe into the system and
     * calling the getRecipe() method to make sure it is in the system. It then
     * adds another recipe and calls the getRecipe() method to make sure that
     * recipe was also added to the system without error. It then tries to
     * retrieve a recipe that was not added to the system to make sure it
     * returns a not found status.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testGetRecipe () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        assertTrue( recipe.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Sugar", 1 ) ) );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        mvc.perform( get( "/api/v1/recipes/" + recipe.getName() ) ).andExpect( status().isOk() );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Mocha" );

        assertTrue( recipe2.addIngredient( new Ingredient( "Chocolate", 20 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Coffee", 2 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Milk", 10 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Sugar", 4 ) ) );

        recipe2.setPrice( 2 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, (int) service.count() );

        mvc.perform( get( "/api/v1/recipes/" + recipe2.getName() ) ).andExpect( status().isOk() );

        final String notAddedRecipe = "NotAddedRecipe";
        mvc.perform( get( "/api/v1/recipes/" + notAddedRecipe ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests the editRecipe() method by adding a recipe into the system and
     * calling the editRecipe() method to with another recipe to make sure it
     * has been updated.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testEditRecipe () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        assertTrue( recipe.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Sugar", 1 ) ) );

        recipe.setPrice( 5 );

        service.save( recipe );

        final String name = "Delicious Not-Coffee";

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Mocha" );

        assertTrue( recipe2.addIngredient( new Ingredient( "Chocolate", 20 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Coffee", 2 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Milk", 10 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Sugar", 4 ) ) );

        mvc.perform( post( String.format( "/api/v1/recipes/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        Assertions.assertEquals( 20, service.findByName( "Mocha" ).getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 2, service.findByName( "Mocha" ).getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 10, service.findByName( "Mocha" ).getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 4, service.findByName( "Mocha" ).getIngredients().get( 3 ).getAmount() );

        mvc.perform( post( String.format( "/api/v1/recipes/random" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isConflict() );

    }

}
