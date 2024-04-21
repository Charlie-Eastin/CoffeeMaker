package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * An ingredient object for the CoffeeMaker. Each ingredient is tied to the
 * database using Hibernate libraries. See IngredientRepository and
 * IngredientService for the other two pieces used for database support.
 */
@Entity
public class Ingredient extends DomainObject {

    /** ID for an ingredient */
    @Id
    @GeneratedValue
    private Long    id;

    /** The name of the ingredient */
    private String  name;

    /**
     * How much of an ingredient there is (in a recipe, inventory, or otherwise)
     */
    @Min ( 0 )
    private Integer amount;

    /**
     * Constructor for Ingredient with parameters
     *
     * @param name
     *            the name of the ingredient
     * @param amount
     *            how much of the ingredient
     */
    public Ingredient ( final String name, final int amount ) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * Default constructor for Ingredient
     */
    public Ingredient () {

    }

    /**
     * Generates a string representation of the ingredient with all fields
     * listed (used for testing)
     */
    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

    /**
     * Getter for name field
     *
     * @return name
     */
    public String getName () {
        return name;
    }

    /**
     * Setter for name field
     *
     * @param ingredient
     *            name to set
     */
    public void setName ( final String ingredient ) {
        this.name = ingredient;
    }

    /**
     * Getter for amount field
     *
     * @return amount
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Setter for amount field
     *
     * @param amount
     *            amount to set
     */
    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

    /**
     * Setter for ID
     *
     * @param id
     *            id to set
     */
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
