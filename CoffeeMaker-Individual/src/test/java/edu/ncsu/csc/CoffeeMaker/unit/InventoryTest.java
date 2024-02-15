package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.LinkedList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        inventoryService.deleteAll();
        final Inventory ivt = inventoryService.getInventory();
        ivt.setIngredient( "Chocolate", 500 );
        ivt.setIngredient( "Coffee", 500 );
        ivt.setIngredient( "Milk", 500 );
        ivt.setIngredient( "Sugar", 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        // Chocolate, Coffee, Milk, Sugar

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( i.getIngredients().get( 0 ).getName(), 10 ) ); // Chocolate
        recipe.addIngredient( new Ingredient( "Coffee", 5 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) ); // Milk
        recipe.addIngredient( new Ingredient( i.getIngredients().get( 3 ).getName(), 1 ) ); // Sugar

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 495, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 480, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 499, i.getIngredients().get( 3 ).getAmount() );

        Assertions.assertEquals( i.toString(), "Chocolate: 490\n" + "Coffee: 495\n" + "Milk: 480\n" + "Sugar: 499\n" );
    }

    @Test
    @Transactional
    public void testNotEnoughInventory () {
        // final Inventory i = inventoryService.getInventory();

        // final List<Ingredient> invList = new LinkedList<>();
        final Inventory i = inventoryService.getInventory();
        i.setIngredient( "Chocolate", 1 );
        i.setIngredient( "Coffee", 1 );
        i.setIngredient( "Milk", 4 );
        i.setIngredient( "Sugar", 4 );
        // invList.add( new Ingredient( "Coffee", 1 ) );
        // invList.add( new Ingredient( "Chocolate", 4 ) );
        // invList.add( new Ingredient( "Milk", 2 ) );
        // invList.add( new Ingredient( "Sugar", 3 ) );
        // final Inventory i = new Inventory( invList );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );

        recipe.setPrice( 5 );

        Assertions.assertFalse( i.useIngredients( recipe ) );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Delicious Not-Coffee" );
        recipe2.addIngredient( new Ingredient( "Chocolate", 4 ) );
        recipe2.addIngredient( new Ingredient( "Coffee", 2 ) );
        recipe2.addIngredient( new Ingredient( "Milk", 3 ) );
        recipe2.addIngredient( new Ingredient( "Sugar", 3 ) );
        recipe2.setPrice( 5 );

        Assertions.assertFalse( i.useIngredients( recipe2 ) );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        // Chocolate, Coffee, Milk, Sugar

        Assertions.assertEquals( 1, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 1, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 4, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 4, i.getIngredients().get( 3 ).getAmount() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();
        ivt.setIngredient( "Chocolate", 502 );
        ivt.setIngredient( "Coffee", 505 );
        ivt.setIngredient( "Milk", 503 );
        ivt.setIngredient( "Sugar", 507 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        // Chocolate, Coffee, Milk, Sugar
        Assertions.assertEquals( 502, ivt.getIngredients().get( 0 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 505, ivt.getIngredients().get( 1 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 503, ivt.getIngredients().get( 2 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 507, ivt.getIngredients().get( 3 ).getAmount(),
                "Adding to the inventory should result in correctly-updated valueschocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.setIngredient( "Coffee", -5 );
            ivt.setIngredient( "Milk", 3 );
            ivt.setIngredient( "Sugar", 7 );
            ivt.setIngredient( "Chocolate", 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee shouldresult in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.setIngredient( "Coffee", 5 );
            ivt.setIngredient( "Milk", -3 );
            ivt.setIngredient( "Sugar", 7 );
            ivt.setIngredient( "Chocolate", 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.setIngredient( "Coffee", 5 );
            ivt.setIngredient( "Milk", 3 );
            ivt.setIngredient( "Sugar", -7 );
            ivt.setIngredient( "Chocolate", 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.setIngredient( "Coffee", 5 );
            ivt.setIngredient( "Milk", 3 );
            ivt.setIngredient( "Sugar", 7 );
            ivt.setIngredient( "Chocolate", -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddIngredients () {
        final Inventory ivt = inventoryService.getInventory();

        final List<Ingredient> list = new LinkedList<Ingredient>();
        list.add( new Ingredient( "Chocolate", 1 ) );
        list.add( new Ingredient( "Coffee", 2 ) );
        list.add( new Ingredient( "Milk", 3 ) );
        list.add( new Ingredient( "Sugar", 4 ) );

        ivt.addIngredients( list );

        Assertions.assertEquals( "Chocolate", ivt.getIngredients().get( 0 ).getName() );
        Assertions.assertEquals( "Coffee", ivt.getIngredients().get( 1 ).getName() );
        Assertions.assertEquals( "Milk", ivt.getIngredients().get( 2 ).getName() );
        Assertions.assertEquals( "Sugar", ivt.getIngredients().get( 3 ).getName() );
    }

    @Test
    @Transactional
    public void checkIngredientsPositiveOnly () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setIngredient( "Chocolate", 5 );
        ivt.setIngredient( "Coffee", 3 );
        ivt.setIngredient( "Milk", 7 );
        ivt.setIngredient( "Sugar", 2 );
        inventoryService.save( ivt );

        Assertions.assertEquals( 5, ivt.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 3, ivt.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 7, ivt.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 2, ivt.getIngredients().get( 3 ).getAmount() );
    }

    @Test
    @Transactional
    public void checkIngredientsError () {
        final Inventory ivt = inventoryService.getInventory();

        Assertions.assertFalse( ivt.setIngredient( "Chocolate", -5 ) );

        Assertions.assertFalse( ivt.setIngredient( "Milk", -1 ) );

        Assertions.assertFalse( ivt.setIngredient( "Sugar", -1999999 ) );

        Assertions.assertFalse( ivt.setIngredient( "Milk", -2 ) );

        Assertions.assertFalse( ivt.setIngredient( Integer.toString( 3 ), -5 ) );

        // Ensure amount is unchanged

        Assertions.assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount() );

    }

    // public void checkCoffee () {
    // final Inventory ivt = inventoryService.getInventory();
    //
    // Assertions.assertEquals( 3, (int) ivt.checkCoffee( "3" ), "Trying to
    // validate 3 coffee units." );
    // Assertions.assertEquals( 10, (int) ivt.checkCoffee( "010" ), "Trying to
    // validate 10 coffee units." );
    //
    // Assertions.assertEquals( 3, (int) ivt.checkChocolate( "3" ), "Trying to
    // validate 3 chocolate units." );
    // Assertions.assertEquals( 10, (int) ivt.checkChocolate( "010" ), "Trying
    // to validate 10 chocolate units." );
    //
    // Assertions.assertEquals( 3, (int) ivt.checkSugar( "3" ), "Trying to
    // validate 3 sugar units." );
    // Assertions.assertEquals( 10, (int) ivt.checkSugar( "010" ), "Trying to
    // validate 10 sugar units." );
    //
    // Assertions.assertEquals( 3, (int) ivt.checkMilk( "3" ), "Trying to
    // validate 3 milk units." );
    // Assertions.assertEquals( 10, (int) ivt.checkMilk( "010" ), "Trying to
    // validate 10 milk units." );
    //
    // ////////////////////////
    // // Invalid values
    // ///////////////////////
    //
    // // Alphabetic values
    //
    // final Exception e1 = Assertions.assertThrows(
    // IllegalArgumentException.class,
    // () -> ivt.checkChocolate( "abc" ) );
    // Assertions.assertEquals( e1.getMessage(), "Units of chocolate must be a
    // positive integer" );
    //
    // final Exception e2 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkCoffee( "c" ) );
    // Assertions.assertEquals( e2.getMessage(), "Units of coffee must be a
    // positive integer" );
    //
    // final Exception e3 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkMilk( "abc" ) );
    // Assertions.assertEquals( e3.getMessage(), "Units of milk must be a
    // positive integer" );
    //
    // final Exception e4 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkSugar( "c" ) );
    // Assertions.assertEquals( e4.getMessage(), "Units of sugar must be a
    // positive integer" );
    //
    // // Negative values
    //
    // final Exception e5 = Assertions.assertThrows(
    // IllegalArgumentException.class,
    // () -> ivt.checkChocolate( "-3" ) );
    // Assertions.assertEquals( e5.getMessage(), "Units of chocolate must be a
    // positive integer" );
    //
    // final Exception e6 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkCoffee( "-3" ) );
    // Assertions.assertEquals( e6.getMessage(), "Units of coffee must be a
    // positive integer" );
    //
    // final Exception e7 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkMilk( "-3" ) );
    // Assertions.assertEquals( e7.getMessage(), "Units of milk must be a
    // positive integer" );
    //
    // final Exception e8 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkSugar( "-3" ) );
    // Assertions.assertEquals( e8.getMessage(), "Units of sugar must be a
    // positive integer" );
    //
    // }
    //
    // public void checkCoffee () {
    // final Inventory ivt = inventoryService.getInventory();
    //
    // Assertions.assertEquals( 3, (int) ivt.checkCoffee( "3" ), "Trying to
    // validate 3 coffee units." );
    // Assertions.assertEquals( 10, (int) ivt.checkCoffee( "010" ), "Trying to
    // validate 10 coffee units." );
    //
    // Assertions.assertEquals( 3, (int) ivt.checkChocolate( "3" ), "Trying to
    // validate 3 chocolate units." );
    // Assertions.assertEquals( 10, (int) ivt.checkChocolate( "010" ), "Trying
    // to validate 10 chocolate units." );
    //
    // Assertions.assertEquals( 3, (int) ivt.checkSugar( "3" ), "Trying to
    // validate 3 sugar units." );
    // Assertions.assertEquals( 10, (int) ivt.checkSugar( "010" ), "Trying to
    // validate 10 sugar units." );
    //
    // Assertions.assertEquals( 3, (int) ivt.checkMilk( "3" ), "Trying to
    // validate 3 milk units." );
    // Assertions.assertEquals( 10, (int) ivt.checkMilk( "010" ), "Trying to
    // validate 10 milk units." );
    //
    // ////////////////////////
    // // Invalid values
    // ///////////////////////
    //
    // // Alphabetic values
    //
    // final Exception e1 = Assertions.assertThrows(
    // IllegalArgumentException.class,
    // () -> ivt.checkChocolate( "abc" ) );
    // Assertions.assertEquals( e1.getMessage(), "Units of chocolate must be a
    // positive integer" );
    //
    // final Exception e2 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkCoffee( "c" ) );
    // Assertions.assertEquals( e2.getMessage(), "Units of coffee must be a
    // positive integer" );
    //
    // final Exception e3 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkMilk( "abc" ) );
    // Assertions.assertEquals( e3.getMessage(), "Units of milk must be a
    // positive integer" );
    //
    // final Exception e4 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkSugar( "c" ) );
    // Assertions.assertEquals( e4.getMessage(), "Units of sugar must be a
    // positive integer" );
    //
    // // Negative values
    //
    // final Exception e5 = Assertions.assertThrows(
    // IllegalArgumentException.class,
    // () -> ivt.checkChocolate( "-3" ) );
    // Assertions.assertEquals( e5.getMessage(), "Units of chocolate must be a
    // positive integer" );
    //
    // final Exception e6 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkCoffee( "-3" ) );
    // Assertions.assertEquals( e6.getMessage(), "Units of coffee must be a
    // positive integer" );
    //
    // final Exception e7 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkMilk( "-3" ) );
    // Assertions.assertEquals( e7.getMessage(), "Units of milk must be a
    // positive integer" );
    //
    // final Exception e8 = Assertions.assertThrows(
    // IllegalArgumentException.class, () -> ivt.checkSugar( "-3" ) );
    // Assertions.assertEquals( e8.getMessage(), "Units of sugar must be a
    // positive integer" );
    //
    // }

}
