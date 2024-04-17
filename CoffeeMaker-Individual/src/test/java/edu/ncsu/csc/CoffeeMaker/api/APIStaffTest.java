package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIStaffTest {

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
    private RecipeService         recipeService;

    @Autowired
    private InventoryService      inventoryService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        recipeService.deleteAll();
        inventoryService.deleteAll();
    }

    @Test
    @Transactional
    public void testInventoryAPI () throws Exception {

        final List<Ingredient> list = new LinkedList<Ingredient>();

        list.add( new Ingredient( "Milk", 5 ) );
        list.add( new Ingredient( "Coffee", 4 ) );
        list.add( new Ingredient( "Tea", 2 ) );
        list.add( new Ingredient( "Sugar", 1 ) );

        final Inventory inventory = new Inventory( list );

        final List<Ingredient> emptyList = new LinkedList<Ingredient>();
        final Inventory inventory2 = new Inventory( emptyList );

        // Test with populated list.
        mvc.perform( put( "/api/v1/staff/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

        assertEquals( 1, (int) inventoryService.count() );

        // Check inventory amounts
        assertEquals( inventoryService.getInventory().getIngredients().size(), 4 );
        assertEquals( inventoryService.getInventory().getIngredients().get( 0 ).getName(), "Milk" );
        assertEquals( inventoryService.getInventory().getIngredients().get( 1 ).getName(), "Coffee" );
        assertEquals( inventoryService.getInventory().getIngredients().get( 2 ).getName(), "Tea" );
        assertEquals( inventoryService.getInventory().getIngredients().get( 3 ).getName(), "Sugar" );

        // Try it again with a list of zero ingredients.
        mvc.perform( put( "/api/v1/staff/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory2 ) ) ).andExpect( status().isOk() );

        // Inventory should still be there.
        assertEquals( 1, (int) inventoryService.count() );
        // But should have no ingredients.
        assertEquals( inventoryService.getInventory().getIngredients().size(), 0 );

    }

    @Test
    @Transactional
    public void testStaffEditRecipe () throws Exception {

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        assertTrue( recipe.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( recipe.addIngredient( new Ingredient( "Sugar", 1 ) ) );

        recipe.setPrice( 5 );

        recipeService.save( recipe );

        final String name = "Delicious Not-Coffee";

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Mocha" );

        assertTrue( recipe2.addIngredient( new Ingredient( "Chocolate", 20 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Coffee", 2 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Milk", 10 ) ) );
        assertTrue( recipe2.addIngredient( new Ingredient( "Sugar", 4 ) ) );

        mvc.perform( post( String.format( "/api/v1/staff/recipes/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) recipeService.count() );

        Assertions.assertEquals( 20, recipeService.findByName( "Mocha" ).getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 2, recipeService.findByName( "Mocha" ).getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 10, recipeService.findByName( "Mocha" ).getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 4, recipeService.findByName( "Mocha" ).getIngredients().get( 3 ).getAmount() );

        mvc.perform( post( String.format( "/api/v1/staff/recipes/random" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isConflict() );

        // Recipe with no ingredients should not be allowed.
        final Recipe noIngredients = new Recipe();
        noIngredients.setName( "Bad Recipe" );
        mvc.perform( post( String.format( "/api/v1/staff/recipes/Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( noIngredients ) ) ).andExpect( status().isConflict() );
    }

    @Test
    @Transactional
    public void testStaffAddRecipe () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r1.setPrice( 5 );

        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r2.setPrice( 5 );

        mvc.perform( post( "/api/v1/staff/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, recipeService.findAll().size(), "There should only one recipe in the CoffeeMaker" );

    }

    @Test
    @Transactional
    public void testStaffAddRecipeCap () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r1.setPrice( 5 );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r2.setPrice( 5 );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Coffee" );
        assertTrue( r3.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r3.setPrice( 5 );
        recipeService.save( r3 );

        Assertions.assertEquals( 3, recipeService.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe();
        r4.setName( "Hot Chocolate" );
        assertTrue( r4.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r4.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r4.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r4.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r4.setPrice( 5 );

        mvc.perform( post( "/api/v1/staff/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, recipeService.count(), "Insufficient space in recipe book for recipe" );
    }

    @Test
    @Transactional
    public void testDeleteRecipeAPI () throws Exception {

        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r1.setPrice( 5 );

        recipeService.save( r1 );

        Assertions.assertEquals( 1, recipeService.findAll().size(),
                "There should only be one recipe in the CoffeeMaker" );

        mvc.perform( delete( "/api/v1/staff/recipes/Coffee" ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no recipe in the CoffeeMaker" );
        assertTrue( recipeService.findByName( "Coffee" ) == null );

        final Recipe r2 = new Recipe();
        r2.setName( "Hot Chocolate" );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r2.setPrice( 5 );

        final Recipe r3 = new Recipe();
        r3.setName( "Hot Mocha" );
        assertTrue( r3.addIngredient( new Ingredient( "Chocolate", 10 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Coffee", 20 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Milk", 5 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        r3.setPrice( 5 );

        recipeService.save( r3 );
        recipeService.save( r2 );

        Assertions.assertEquals( 2, recipeService.findAll().size(),
                "There should only be two recipes in the CoffeeMaker" );

        mvc.perform( delete( "/api/v1/staff/recipes/Hot Mocha" ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, recipeService.findAll().size(), "There should be one recipe in the CoffeeMaker" );

        assertTrue( recipeService.findByName( "Hot Chocolate" ) != null );
        assertTrue( recipeService.findByName( "Hot Mocha" ) == null );

    }

}
