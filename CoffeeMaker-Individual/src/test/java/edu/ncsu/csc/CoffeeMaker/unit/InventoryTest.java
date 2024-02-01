package edu.ncsu.csc.CoffeeMaker.unit;

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
        final Inventory ivt = inventoryService.getInventory();

        ivt.setChocolate( 500 );
        ivt.setCoffee( 500 );
        ivt.setMilk( 500 );
        ivt.setSugar( 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getChocolate() );
        Assertions.assertEquals( 480, (int) i.getMilk() );
        Assertions.assertEquals( 495, (int) i.getSugar() );
        Assertions.assertEquals( 499, (int) i.getCoffee() );
    }

    @Test
    @Transactional
    public void testNotEnoughInventory () {
        final Inventory i = inventoryService.getInventory();

        i.setChocolate( 4 );
        i.setCoffee( 1 );
        i.setMilk( 2 );
        i.setSugar( 3 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );

        Assertions.assertFalse( i.useIngredients( recipe ) );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Delicious Not-Coffee" );
        recipe2.setChocolate( 4 );
        recipe2.setMilk( 3 );
        recipe2.setSugar( 3 );
        recipe2.setCoffee( 1 );

        recipe2.setPrice( 5 );

        Assertions.assertFalse( i.useIngredients( recipe2 ) );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 4, (int) i.getChocolate() );
        Assertions.assertEquals( 2, (int) i.getMilk() );
        Assertions.assertEquals( 3, (int) i.getSugar() );
        Assertions.assertEquals( 1, (int) i.getCoffee() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 5, 3, 7, 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getCoffee(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getMilk(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getSugar(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getChocolate(),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( -5, 3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, -3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, -7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, 7, -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void checkIngredientsPositiveOnly () {
        final Inventory ivt = inventoryService.getInventory();

        final int chocolateInt = ivt.checkChocolate( "5" );
        final int coffeeInt = ivt.checkCoffee( "4" );
        final int milkInt = ivt.checkMilk( "3" );
        final int sugarInt = ivt.checkSugar( "2" );

        Assertions.assertEquals( 5, chocolateInt );
        Assertions.assertEquals( 4, coffeeInt );
        Assertions.assertEquals( 3, milkInt );
        Assertions.assertEquals( 2, sugarInt );
    }

    @Test
    @Transactional
    public void checkIngredientsError () {
        final Inventory ivt = inventoryService.getInventory();

        try {

            final int chocolateInt = ivt.checkChocolate( "-5" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( iae.getMessage(), "Units of chocolate must be a positive integer" );
        }

        try {
            final int coffeeInt = ivt.checkCoffee( "-4" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( iae.getMessage(), "Units of coffee must be a positive integer" );
        }

        try {
            final int milkInt = ivt.checkMilk( "-3" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( iae.getMessage(), "Units of milk must be a positive integer" );
        }

        try {
            final int sugarInt = ivt.checkSugar( "-2" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( iae.getMessage(), "Units of sugar must be a positive integer" );
        }
    }

    public void checkCoffee () {
        final Inventory ivt = inventoryService.getInventory();

        Assertions.assertEquals( 3, (int) ivt.checkCoffee( "3" ), "Trying to validate 3 coffee units." );
        Assertions.assertEquals( 10, (int) ivt.checkCoffee( "010" ), "Trying to validate 10 coffee units." );

        Assertions.assertEquals( 3, (int) ivt.checkChocolate( "3" ), "Trying to validate 3 chocolate units." );
        Assertions.assertEquals( 10, (int) ivt.checkChocolate( "010" ), "Trying to validate 10 chocolate units." );

        Assertions.assertEquals( 3, (int) ivt.checkSugar( "3" ), "Trying to validate 3 sugar units." );
        Assertions.assertEquals( 10, (int) ivt.checkSugar( "010" ), "Trying to validate 10 sugar units." );

        Assertions.assertEquals( 3, (int) ivt.checkMilk( "3" ), "Trying to validate 3 milk units." );
        Assertions.assertEquals( 10, (int) ivt.checkMilk( "010" ), "Trying to validate 10 milk units." );

        ////////////////////////
        // Invalid values
        ///////////////////////

        // Alphabetic values

        final Exception e1 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkChocolate( "abc" ) );
        Assertions.assertEquals( e1.getMessage(), "Units of chocolate must be a positive integer" );

        final Exception e2 = Assertions.assertThrows( IllegalArgumentException.class, () -> ivt.checkCoffee( "c" ) );
        Assertions.assertEquals( e2.getMessage(), "Units of coffee must be a positive integer" );

        final Exception e3 = Assertions.assertThrows( IllegalArgumentException.class, () -> ivt.checkMilk( "abc" ) );
        Assertions.assertEquals( e3.getMessage(), "Units of milk must be a positive integer" );

        final Exception e4 = Assertions.assertThrows( IllegalArgumentException.class, () -> ivt.checkSugar( "c" ) );
        Assertions.assertEquals( e4.getMessage(), "Units of sugar must be a positive integer" );

        // Negative values

        final Exception e5 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkChocolate( "-3" ) );
        Assertions.assertEquals( e5.getMessage(), "Units of chocolate must be a positive integer" );

        final Exception e6 = Assertions.assertThrows( IllegalArgumentException.class, () -> ivt.checkCoffee( "-3" ) );
        Assertions.assertEquals( e6.getMessage(), "Units of coffee must be a positive integer" );

        final Exception e7 = Assertions.assertThrows( IllegalArgumentException.class, () -> ivt.checkMilk( "-3" ) );
        Assertions.assertEquals( e7.getMessage(), "Units of milk must be a positive integer" );

        final Exception e8 = Assertions.assertThrows( IllegalArgumentException.class, () -> ivt.checkSugar( "-3" ) );
        Assertions.assertEquals( e8.getMessage(), "Units of sugar must be a positive integer" );

    }

}
