package edu.ncsu.csc.CoffeeMaker;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipesSimple {

    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testCreateRecipes () {

        recipeService.deleteAll();

        final Recipe r1 = new Recipe();
        final Ingredient chocolate = new Ingredient( "Chocolate", 0 );
        final Ingredient milk = new Ingredient( "Milk", 2 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        r1.addIngredient( chocolate );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( coffee );
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );

        final Recipe r2 = new Recipe();
        r1.addIngredient( chocolate );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( coffee );
        r1.setName( "Mocha" );
        r1.setPrice( 3 );

        recipeService.save( r1 );
        recipeService.save( r2 );

        Assert.assertEquals( "Creating two recipes should results in two recipes in the database", 2,
                recipeService.count() );

    }

}
