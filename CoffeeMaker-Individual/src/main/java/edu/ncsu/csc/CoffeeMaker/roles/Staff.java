package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.List;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

public class Staff extends User {
    private String name;
    private String password;
    private String type;

    public boolean updateInventory ( final List<Ingredient> ingredientList ) {

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

}
