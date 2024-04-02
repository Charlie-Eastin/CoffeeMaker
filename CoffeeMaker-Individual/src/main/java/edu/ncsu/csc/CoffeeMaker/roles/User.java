package edu.ncsu.csc.CoffeeMaker.roles;

import java.io.Serializable;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;

public class User extends DomainObject {
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

    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }
}
