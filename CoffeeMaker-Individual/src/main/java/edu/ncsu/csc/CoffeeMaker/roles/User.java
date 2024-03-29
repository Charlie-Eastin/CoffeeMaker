package edu.ncsu.csc.CoffeeMaker.roles;

public class User {
    private String name;
    private String password;
    private String type;

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
