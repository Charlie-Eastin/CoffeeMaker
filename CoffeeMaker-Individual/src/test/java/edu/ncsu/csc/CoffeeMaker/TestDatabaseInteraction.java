package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the Database interaction for the RecipeService class.
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    /** Reference to the RecipeService */
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
        // Create a recipe for testing
        final Recipe mochaRecipe = new Recipe();
        mochaRecipe.setName( "Mocha" );
        mochaRecipe.setPrice( 350 );
        mochaRecipe.setCoffee( 2 );
        mochaRecipe.setMilk( 1 );
        mochaRecipe.setSugar( 1 );
        mochaRecipe.setChocolate( 1 );
        recipeService.save( mochaRecipe );

        // Test that we can get the recipe from the database.
        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );
        assertAll( "Recipe", () -> assertEquals( mochaRecipe.getName(), dbRecipe.getName() ),
                () -> assertEquals( mochaRecipe.getPrice(), dbRecipe.getPrice() ),
                () -> assertEquals( mochaRecipe.getCoffee(), dbRecipe.getCoffee() ),
                () -> assertEquals( mochaRecipe.getMilk(), dbRecipe.getMilk() ),
                () -> assertEquals( mochaRecipe.getSugar(), dbRecipe.getSugar() ),
                () -> assertEquals( mochaRecipe.getChocolate(), dbRecipe.getChocolate() ) );

        // Test retrieving a recipe by name
        final Recipe dbRecipeByName = recipeService.findByName( "Mocha" );
        assertAll( "Recipe", () -> assertEquals( mochaRecipe.getName(), dbRecipeByName.getName() ),
                () -> assertEquals( mochaRecipe.getPrice(), dbRecipeByName.getPrice() ),
                () -> assertEquals( mochaRecipe.getCoffee(), dbRecipeByName.getCoffee() ),
                () -> assertEquals( mochaRecipe.getMilk(), dbRecipeByName.getMilk() ),
                () -> assertEquals( mochaRecipe.getSugar(), dbRecipeByName.getSugar() ),
                () -> assertEquals( mochaRecipe.getChocolate(), dbRecipeByName.getChocolate() ) );

        // Test editing an object
        mochaRecipe.setSugar( 27 );
        recipeService.save( mochaRecipe );

        final List<Recipe> dbRecipes2 = recipeService.findAll();

        assertEquals( 1, dbRecipes2.size() );

        final Recipe dbRecipe2 = dbRecipes2.get( 0 );
        assertAll( "Recipe", () -> assertEquals( mochaRecipe.getName(), dbRecipe2.getName() ),
                () -> assertEquals( mochaRecipe.getPrice(), dbRecipe2.getPrice() ),
                () -> assertEquals( mochaRecipe.getCoffee(), dbRecipe2.getCoffee() ),
                () -> assertEquals( mochaRecipe.getMilk(), dbRecipe2.getMilk() ),
                () -> assertEquals( 27, dbRecipe2.getSugar() ),
                () -> assertEquals( mochaRecipe.getChocolate(), dbRecipe2.getChocolate() ) );
    }

}
