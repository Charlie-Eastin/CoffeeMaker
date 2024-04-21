package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Class to test the interaction between the different items in the database
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {
        final Recipe r1 = new Recipe();
        r1.setName( "Recipe1" );

        r1.setPrice( 50 );

        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Tea", 6 ) ) );

        recipeService.save( r1 );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r1.getName(), dbRecipe.getName() );
        assertEquals( r1.getIngredients(), dbRecipe.getIngredients() );
        assertEquals( r1.getPrice(), dbRecipe.getPrice() );

        assertEquals( recipeService.findByName( "Recipe1" ), r1 );

        dbRecipe.setPrice( 15 );
        dbRecipe.setIngredient( new Ingredient( "Coffee", 3 ) );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        assertEquals( r1.getPrice(), 15 );
        assertEquals( r1.getIngredients().get( 0 ).getAmount(), 3 );
        assertEquals( 3, recipeService.findAll().get( 0 ).getIngredients().get( 0 ).getAmount() );
    }

    /**
     * Tests the delete function in the recipes class
     */
    @Test
    @Transactional
    public void testDelete () {
        final Recipe r = new Recipe();
        final Recipe r2 = new Recipe();
        final Recipe r3 = new Recipe();

        // Represents recipe 1
        r.setName( "Recipe1" );
        r.setPrice( 5 );
        assertTrue( r.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Tea", 6 ) ) );

        // Represents recipe 2
        r2.setName( "Recipe2" );
        r2.setPrice( 5 );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Tea", 6 ) ) );

        // Represents recipe 3
        r3.setName( "Recipe3" );
        r3.setPrice( 5 );
        assertTrue( r3.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Tea", 6 ) ) );

        // Saves each recipe to recipe service
        recipeService.save( r );
        recipeService.save( r2 );
        recipeService.save( r3 );

        // Finds all the recipes and adds them to dbRecipes
        List<Recipe> dbRecipes = recipeService.findAll();

        // Ensures there are 3 recipes
        assertEquals( 3, dbRecipes.size() );

        // Deletes the first recipe
        recipeService.delete( r );

        // Gets all the recipes again
        dbRecipes = recipeService.findAll();

        // Ensures there are 2 recipes
        assertEquals( 2, dbRecipes.size() );

    }

    /**
     * Tests the delete function in the recipes class
     */
    @Test
    @Transactional
    public void testDeleteAll () {
        final Recipe r = new Recipe();
        final Recipe r2 = new Recipe();
        final Recipe r3 = new Recipe();

        // Represents recipe 1
        r.setName( "Recipe1" );
        r.setPrice( 5 );
        assertTrue( r.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Tea", 6 ) ) );

        // Represents recipe 2
        r2.setName( "Recipe2" );
        r2.setPrice( 5 );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Tea", 6 ) ) );

        // Represents recipe 3
        r3.setName( "Recipe3" );
        r3.setPrice( 5 );
        assertTrue( r3.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Milk", 2 ) ) );
        assertTrue( r3.addIngredient( new Ingredient( "Tea", 6 ) ) );

        // Saves each recipe to recipe service
        recipeService.save( r );
        recipeService.save( r2 );
        recipeService.save( r3 );

        List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 3, dbRecipes.size() );

        dbRecipes = recipeService.findAll();

        // Deletes all the recipes
        recipeService.deleteAll();

        dbRecipes = recipeService.findAll();

        // Ensures there are 0 recipes now
        assertEquals( 0, dbRecipes.size() );
    }

    /**
     * Tests the findByID method in recipeService by saving a recipe to the
     * database and ensuring the correct recipe is retrieved when searching by
     * ID
     */
    @Test
    @Transactional
    public void testFindByID () {
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        assertTrue( r.addIngredient( new Ingredient( "Chocolate", 15 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Milk", 20 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Sugar", 10 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        r.setPrice( 20 );
        recipeService.save( r );

        final Recipe foundRecipe = recipeService.findById( r.getId() );
        assertEquals( r.getName(), foundRecipe.getName() );
        assertEquals( r.getIngredients().get( 0 ).getName(), foundRecipe.getIngredients().get( 0 ).getName() );
        assertEquals( r.getPrice(), foundRecipe.getPrice() );

        final Recipe nullRecipe = recipeService.findById( null );
        assertNull( nullRecipe );
    }

    /**
     * testExistsByID() tests the existsByID() method in the Service class by
     * saving a new recipe into the system and then calling the
     * recipeService.existsByID method. It also checks that the method returns
     * null when calling existsByID(null).
     */
    @Test
    @Transactional
    public void testExistsByID () {
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        assertTrue( r.addIngredient( new Ingredient( "Chocolate", 15 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Milk", 20 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Sugar", 10 ) ) );
        assertTrue( r.addIngredient( new Ingredient( "Coffee", 10 ) ) );
        r.setPrice( 20 );
        recipeService.save( r );

        final boolean exists = recipeService.existsById( r.getId() );
        assertTrue( exists );

        final Recipe nullRecipe = recipeService.findById( null );
        assertNull( nullRecipe );
    }
}
