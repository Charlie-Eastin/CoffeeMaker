package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.List;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

public class Staff extends User {
    private String           name;
    private String           password;
    private int              money;
    private String           type;
    private InventoryService inventoryService;
    private RecipeService    recipeService;

    public boolean updateInventory ( final List<Ingredient> ingredientList ) {
        try {
            final Inventory inventory = inventoryService.getInventory();
            inventory.setIngredients( ingredientList );
            inventoryService.save( inventory );
        }
        catch ( final Exception e ) {
            return false;
        }

        return true;
    }

    public boolean editRecipe ( final String name, final int price, final List<Ingredient> ingredientList ) {
        try {
            final Recipe recipe = recipeService.findByName( name );
            final Recipe newRecipe = new Recipe();
            for ( int i = 0; i < ingredientList.size(); i++ ) {
                newRecipe.addIngredient( ingredientList.get( i ) );
            }
            newRecipe.setName( name );
            newRecipe.setPrice( price );
            recipeService.delete( recipe );
            recipeService.save( newRecipe );

        }
        catch ( final Exception e ) {
            return false;
        }
        return true;
    }

    public boolean deleteRecipe ( final String name ) {
        try {
            final Recipe recipe = recipeService.findByName( name );
            recipeService.delete( recipe );
            return true;
        }
        catch ( final Exception e ) {
            return false;
        }

    }

    @Override
    public void setName ( final String name ) {
        this.name = name;
    }

    @Override
    public String getName () {
        return this.name;
    }

    @Override
    public void setType ( final String type ) {
        this.type = type;
    }

    @Override
    public String getType () {
        return this.type;
    }

    @Override
    public void setPassword ( final String password ) {
        this.password = password;
    }

    public void setMoney ( final int money ) {
        this.money = money;
    }

    public int getMoney () {
        return this.money;
    }

}
