package edu.ncsu.csc.CoffeeMaker.roles;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;

/**
 * A model for a User in the CoffeeMaker system
 */
@Entity
public class User extends DomainObject {

    /** A user's ID */
    @Id
    @GeneratedValue
    private Long   id;

    /** Name of the user */
    private String name;

    /** What type of user this is */
    private String type;

    /**
     * Setter for the name field
     *
     * @param name
     *            the new name
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Getter for the name field
     *
     * @return name
     */
    public String getName () {
        return this.name;
    }

    /**
     * Setter for the type field
     *
     * @param type
     *            new type
     */
    public void setType ( final String type ) {
        this.type = type;
    }

    /**
     * Getter for the type field
     *
     * @return type
     */
    public String getType () {
        return this.type;
    }

    /**
     * Setter for the ID field
     *
     * @param id
     *            new id
     */
    public void setId ( final long id ) {
        this.id = id;
    }

    @Override
    public Long getId () {
        return id;
    }

}
