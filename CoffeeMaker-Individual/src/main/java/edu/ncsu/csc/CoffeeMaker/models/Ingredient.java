package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
public class Ingredient extends DomainObject {
    @Id
    @GeneratedValue
    private Long    id;

    private String  name;

    @Min ( 0 )
    private Integer amount;

    public Ingredient ( final String name, final int amount ) {
        this.name = name;
        this.amount = amount;
    }

    public Ingredient () {

    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

    public String getName () {
        return name;
    }

    public void setName ( final String ingredient ) {
        this.name = ingredient;
    }

    public int getAmount () {
        return amount;
    }

    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

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
