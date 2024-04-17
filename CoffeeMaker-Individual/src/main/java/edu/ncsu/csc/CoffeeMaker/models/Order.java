package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.roles.Customer;

@Entity
public class Order extends DomainObject {
    @Id
    @GeneratedValue
    private Long     id;

    private Customer customer;

    private Recipe   recipe;

    private String   status;

    public Order () {
        status = "CREATED";
    }

    public Order ( final Recipe recipe, final Customer customer ) {
        this.customer = customer;
        this.recipe = recipe;
        status = "CREATED";
    }

    public Customer getCustomer () {
        return customer;
    }

    public void setCustomer ( final Customer customer ) {
        this.customer = customer;
    }

    public Recipe getRecipe () {
        return recipe;
    }

    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
    }

    public String getStatus () {
        return status;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public void setStatus ( final String status ) {
        this.status = status;
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
