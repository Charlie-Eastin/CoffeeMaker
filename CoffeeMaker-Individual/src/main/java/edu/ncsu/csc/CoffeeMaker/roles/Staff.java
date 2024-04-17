package edu.ncsu.csc.CoffeeMaker.roles;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A model for the Staff entity in the CoffeeMaker. Represents the access and
 * properties that a Staff using the CoffeeMaker system would have
 */
@Entity
public class Staff extends User {

    /** The ID of a staff member */
    @Id
    @GeneratedValue
    private Long   id;

    /** The name of a staff member */
    private String name;

    /** The type of user this is (STAFF) */
    private String type;

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
