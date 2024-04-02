package edu.ncsu.csc.CoffeeMaker.controllers;

import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

public class APICustomer extends APIController {
    private String           name;
    private String           password;
    private String           type;
    private InventoryService inventoryService;
    private RecipeService    recipeService;

    public void setName ( final String name ) {
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setType ( final String type ) {
        this.type = type;
    }

    public String getType () {
        return this.type;
    }

    public void setPassword ( final String password ) {
        this.password = password;
    }
}
