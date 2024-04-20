package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * An Order for CoffeeMaker. The customer makes an order, the staff can complete
 * the order and the customer picks up the order. Each customer has a list of
 * orders associated with it.
 */
@Entity
@Table ( name = "orders" )
public class Order extends DomainObject {

    /**
     * The order's ID
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long   id;

    // @ManyToOne
    // @JoinColumn ( name = "customer" )
    // private Customer customer;

    /**
     * The Order's recipe
     */
    @OneToOne
    @JoinColumn ( name = "recipe_id", nullable = false )
    private Recipe recipe;

    /**
     * The status of the order (IN PROGRESS or COMPLETE)
     */
    private String status;

    /**
     * Constructor for an order with fields
     */
    public Order () {

    }

    /**
     * Order constructor with fields
     *
     * @param recipe
     *            the order's recipe
     */
    public Order ( final Recipe recipe ) {
        this.recipe = recipe;
        // this.customer = customer;
        this.status = "IN PROGRESS";
    }

    // /**
    // * @param user
    // * @param recipe
    // * @param status
    // */
    // public Order(User user, Recipe recipe, String status) {
    // this.user = user;
    // this.recipe = recipe;
    // this.status = status;
    // }

    // public Customer getCustomer () {
    // return customer;
    // }

    // /**
    // * @param user
    // * the user to set
    // */
    // public void setCustomer ( final Customer customer ) {
    // this.customer = customer;
    // }

    /**
     * Getter for recipe
     *
     * @return the recipe
     */
    public Recipe getRecipe () {
        return recipe;
    }

    /**
     * Setter for recipe
     *
     * @param recipe
     *            the recipe to set
     */
    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
    }

    /**
     * Getter for status
     *
     * @return the status
     */
    public String getStatus () {
        return status;
    }

    /**
     * Setter for status
     *
     * @param status
     *            the status to set
     */
    public void setStatus ( final String status ) {
        this.status = status;
    }

    /**
     * Get the ID of the Order
     *
     * @return the ID
     */
    @Override
    public Serializable getId () {
        return id;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", recipe=" + recipe.toString() + ", status=" + status + "]";
    }

}
