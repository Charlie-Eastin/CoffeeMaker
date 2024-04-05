package edu.ncsu.csc.CoffeeMaker.roles;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer extends User {

    @Id
    @GeneratedValue
    private Long   id;

    private String name;
    private String type;

    private int    money;

    public void setMoney ( final int money ) {
        this.money = money;
    }

    public int getMoney () {
        return this.money;
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
    public void setId ( final long id ) {
        this.id = id;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

}
