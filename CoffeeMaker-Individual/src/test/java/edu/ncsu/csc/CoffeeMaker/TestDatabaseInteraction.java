package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {
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
        final Recipe r = new Recipe();

        r.setName( "Recipe1" );
        r.setChocolate( 5 );
        r.setMilk( 5 );
        r.setSugar( 5 );
        r.setCoffee( 5 );
        r.setPrice( 5 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getChocolate(), dbRecipe.getChocolate() );
        assertEquals( r.getMilk(), dbRecipe.getMilk() );
        assertEquals( r.getSugar(), dbRecipe.getSugar() );
        assertEquals( r.getCoffee(), dbRecipe.getCoffee() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        assertEquals( recipeService.findByName( "Recipe1" ), r );

        dbRecipe.setPrice( 15 );
        dbRecipe.setSugar( 12 );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        assertEquals( r.getPrice(), 15 );
        assertEquals( r.getSugar(), 12 );
        assertEquals( 12, (int) recipeService.findAll().get( 0 ).getSugar() );
    }

    /*
     * Tests the updateRecipes function
     */
    @Test
    @Transactional
    public void testUpdateRecipes () {
        final Recipe r = new Recipe();
        final Recipe r2 = new Recipe();

        r.setName( "Recipe1" );
        r.setChocolate( 5 );
        r.setMilk( 5 );
        r.setSugar( 5 );
        r.setCoffee( 5 );
        r.setPrice( 5 );

        recipeService.save( r );
        final List<Recipe> dbRecipes = recipeService.findAll();

        Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getChocolate(), dbRecipe.getChocolate() );
        assertEquals( r.getMilk(), dbRecipe.getMilk() );
        assertEquals( r.getSugar(), dbRecipe.getSugar() );
        assertEquals( r.getCoffee(), dbRecipe.getCoffee() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        r2.setName( "Recipe2" );
        r2.setChocolate( 6 );
        r2.setMilk( 6 );
        r2.setSugar( 6 );
        r2.setCoffee( 6 );
        r2.setPrice( 6 );

        r.updateRecipe( r2 );

        dbRecipe = dbRecipes.get( 0 );

        assertEquals( r2.getChocolate(), dbRecipe.getChocolate() );
        assertEquals( r2.getMilk(), dbRecipe.getMilk() );
        assertEquals( r2.getSugar(), dbRecipe.getSugar() );
        assertEquals( r2.getCoffee(), dbRecipe.getCoffee() );
        assertEquals( r2.getPrice(), dbRecipe.getPrice() );
    }

    /**
     * testFindByID() tests the findByID() method in the Service class by saving
     * a new recipe into the system and then calling the recipeService.findByID
     * method. It also checks that the method returns null when calling
     * findByID(null).
     */
    @Test
    @Transactional
    public void testFindByID () {
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setChocolate( 15 );
        r.setMilk( 20 );
        r.setSugar( 10 );
        r.setCoffee( 10 );
        r.setPrice( 20 );
        recipeService.save( r );

        final Recipe foundRecipe = recipeService.findById( r.getId() );
        assertEquals( r.getName(), foundRecipe.getName() );
        assertEquals( r.getChocolate(), foundRecipe.getChocolate() );
        assertEquals( r.getMilk(), foundRecipe.getMilk() );
        assertEquals( r.getSugar(), foundRecipe.getSugar() );
        assertEquals( r.getCoffee(), foundRecipe.getCoffee() );
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
        r.setChocolate( 15 );
        r.setMilk( 20 );
        r.setSugar( 10 );
        r.setCoffee( 10 );
        r.setPrice( 20 );
        recipeService.save( r );

        final boolean exists = recipeService.existsById( r.getId() );
        assertTrue( exists );

        final Recipe nullRecipe = recipeService.findById( null );
        assertNull( nullRecipe );
    }
}
