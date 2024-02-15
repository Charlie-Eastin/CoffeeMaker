package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        assertTrue( r1.addIngredient( new Ingredient( "Coffee", 1 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Milk", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Sugar", 0 ) ) );
        assertTrue( r1.addIngredient( new Ingredient( "Chocolate", 0 ) ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        assertTrue( r2.addIngredient( new Ingredient( "Coffee", 1 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Milk", 1 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
        assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 1 ) ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        try {
            final Recipe r1 = new Recipe();
            r1.setName( "Tasty Drink" );
            r1.setPrice( 12 );
            r1.addIngredient( new Ingredient( "Coffee", -12 ) );
            r1.addIngredient( new Ingredient( "Milk", 0 ) );
            r1.addIngredient( new Ingredient( "Sugar", 0 ) );
            r1.addIngredient( new Ingredient( "Chocolate", 0 ) );

            final Recipe r2 = new Recipe();
            r2.setName( "Mocha" );
            r2.setPrice( 1 );
            assertTrue( r2.addIngredient( new Ingredient( "Coffee", 1 ) ) );
            assertTrue( r2.addIngredient( new Ingredient( "Milk", 1 ) ) );
            assertTrue( r2.addIngredient( new Ingredient( "Sugar", 1 ) ) );
            assertTrue( r2.addIngredient( new Ingredient( "Chocolate", 1 ) ) );

            final List<Recipe> recipes = List.of( r1, r2 );

            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        try {
            final Recipe r1 = createRecipe( name, 50, new Ingredient( "Coffee", -3 ), new Ingredient( "Milk", 1 ),
                    new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 2 ) );

            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        try {
            final Recipe r1 = createRecipe( name, 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", -1 ),
                    new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 2 ) );

            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        try {
            final Recipe r1 = createRecipe( name, 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                    new Ingredient( "Sugar", -1 ), new Ingredient( "Chocolate", 2 ) );

            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        try {
            final Recipe r1 = createRecipe( name, 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                    new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", -2 ) );

            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 2 ) );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 1 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, retrieved.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 1, retrieved.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 1, retrieved.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 0, retrieved.getIngredients().get( 3 ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    @Test
    @Transactional
    public void testSaveMultipleRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        final Recipe r2 = createRecipe( "Mocha", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        final List<Recipe> recipes = new LinkedList<>();
        recipes.add( r1 );
        recipes.add( r2 );
        service.saveAll( recipes );

        Assertions.assertEquals( 2, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    @Test
    @Transactional
    public void testRecipeExistsById () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        service.save( r1 );
        assertTrue( service.existsById( r1.getId() ) );
    }

    @Test
    @Transactional
    public void testRecipeFindById () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        service.save( r1 );
        assertEquals( service.findById( r1.getId() ), r1 );
    }

    // @Test
    // @Transactional
    // public void testUpdateRecipe () {
    // Assertions.assertEquals( 0, service.findAll().size(), "There should be no
    // Recipes in the CoffeeMaker" );
    //
    // final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3
    // ), new Ingredient( "Milk", 1 ),
    // new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
    // service.save( r1 );
    //
    // final Recipe r2 = createRecipe( "Mocha", 60, new Ingredient( "Coffee", 4
    // ), new Ingredient( "Milk", 2 ),
    // new Ingredient( "Sugar", 7 ), new Ingredient( "Chocolate", 6 ) );
    //
    // r1.updateRecipe( r2 );
    //
    // service.save( r1 );
    //
    // final Recipe retrieved = service.findByName( "Coffee" );
    //
    // Assertions.assertEquals( 60, (int) retrieved.getPrice() );
    // Assertions.assertEquals( 4, retrieved.getIngredients().get( 0
    // ).getAmount() );
    // Assertions.assertEquals( 2, retrieved.getIngredients().get( 1
    // ).getAmount() );
    // Assertions.assertEquals( 7, retrieved.getIngredients().get( 2
    // ).getAmount() );
    // Assertions.assertEquals( 6, retrieved.getIngredients().get( 3
    // ).getAmount() );
    //
    // Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't
    // duplicate it" );
    //
    // final Recipe r3 = createRecipe( "Coffee", 6, new Ingredient( "Coffee", 2
    // ), new Ingredient( "Milk", 2 ),
    // new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 6 ) );
    //
    // r1.updateRecipe( r3 );
    //
    // service.save( r1 );
    //
    // final Recipe retrieved2 = service.findByName( "Coffee" );
    //
    // Assertions.assertEquals( 6, (int) retrieved2.getPrice() );
    // Assertions.assertEquals( 2, retrieved2.getIngredients().get( 0
    // ).getAmount() );
    // Assertions.assertEquals( 2, retrieved2.getIngredients().get( 1
    // ).getAmount() );
    // Assertions.assertEquals( 1, retrieved2.getIngredients().get( 2
    // ).getAmount() );
    // Assertions.assertEquals( 6, retrieved2.getIngredients().get( 3
    // ).getAmount() );
    //
    // Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't
    // duplicate it" );
    //
    // }

    @SuppressWarnings ( "unlikely-arg-type" )
    @Test
    @Transactional
    public void testEquals () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        final Recipe r2 = createRecipe( "Coffee", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        final Recipe r3 = createRecipe( "Cream", 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        final Recipe r4 = createRecipe( null, 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );
        final Recipe r5 = createRecipe( null, 50, new Ingredient( "Coffee", 3 ), new Ingredient( "Milk", 1 ),
                new Ingredient( "Sugar", 1 ), new Ingredient( "Chocolate", 0 ) );

        Assertions.assertTrue( r1.equals( r2 ) );
        Assertions.assertFalse( r2.equals( r3 ) );
        Assertions.assertFalse( r2.equals( null ) );
        Assertions.assertFalse( r2.equals( new Ingredient( "Test", 1 ) ) );
        Assertions.assertFalse( r2.equals( r4 ) );
        Assertions.assertTrue( r4.equals( r5 ) );

    }

    private Recipe createRecipe ( final String name, final Integer price, final Ingredient coffee,
            final Ingredient milk, final Ingredient sugar, final Ingredient chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        try {
            recipe.addIngredient( coffee );
            recipe.addIngredient( milk );
            recipe.addIngredient( sugar );
            recipe.addIngredient( chocolate );
        }
        catch ( final ConstraintViolationException cvee ) {
            throw new ConstraintViolationException( cvee.getMessage(), null );
        }
        return recipe;
    }

}
