package edu.ncsu.csc.CoffeeMaker.roles;

import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

public class Customer extends User {
    private String           name;
    private String           password;
    private String           type;
    private InventoryService inventoryService;
    private RecipeService    recipeService;

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
}
