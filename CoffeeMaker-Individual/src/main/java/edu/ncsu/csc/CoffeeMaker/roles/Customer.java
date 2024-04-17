package edu.ncsu.csc.CoffeeMaker.roles;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A model for the Customer entity in the CoffeeMaker. Represents the access and
 * properties that a customer using the CoffeeMaker system would have
 */
@Entity
public class Customer extends User {

    /** ID for the customer */
    @Id
    @GeneratedValue
    private Long   id;

    /** Name of the customer */
    private String name;

    /** What type of user this is (Customer) */
    private String type;

    /** The amount of money that the customer has */
    private int    money;

    /**
     * Setter for money field
     *
     * @param money
     *            the amount of money to set
     */
    public void setMoney ( final int money ) {
        this.money = money;
    }

    /**
     * Getter for the money field
     *
     * @return money
     */
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
