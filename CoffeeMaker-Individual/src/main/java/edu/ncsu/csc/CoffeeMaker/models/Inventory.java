package edu.ncsu.csc.CoffeeMaker.models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long     id;

    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     */
    public Inventory ( final List<Ingredient> list ) {
        ingredients = new LinkedList<Ingredient>( list );
    }

    public void setAmount ( final int id, final int amount ) {
        this.ingredients.get( id ).setAmount( amount );
    }

    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        final List<Ingredient> list = r.getIngredients();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            for ( int j = 0; j < list.size(); j++ ) {
                if ( ingredients.get( i ).getName().equals( list.get( j ).getName() ) ) {
                    if ( ingredients.get( i ).getAmount() < list.get( j ).getAmount() ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            final List<Ingredient> list = r.getIngredients();
            for ( int i = 0; i < ingredients.size(); i++ ) {
                for ( int j = 0; j < list.size(); j++ ) {
                    if ( ingredients.get( i ).getName().equals( list.get( j ).getName() ) ) {
                        setAmount( i, ingredients.get( i ).getAmount() - list.get( j ).getAmount() );
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to existing ingredients in the inventory
     *
     * @param list
     *            list of ingredients
     *
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final List<Ingredient> list ) {
        if ( list == null ) {
            return false;
        }
        for ( int i = 0; i < list.size(); i++ ) {
            if ( list.get( i ).getAmount() < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }
        }

        for ( int i = 0; i < list.size(); i++ ) {
            boolean found = false;
            for ( int j = 0; j < ingredients.size(); j++ ) {
                if ( list.get( i ).getName().equals( ingredients.get( j ).getName() ) ) {
                    setAmount( i, list.get( i ).getAmount() + ingredients.get( j ).getAmount() );
                    found = true;
                    continue;
                }
            }
            if ( found == false ) {
                ingredients.add( list.get( i ) );
            }
        }

        return true;
    }

    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Adds an ingredient to the inventory.
     *
     * @param name
     * @param amount
     * @return
     */
    public boolean setIngredient ( final String name, final int amount ) {

        if ( amount < 0 ) {
            return false;
        }
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( name ) ) {
                ingredients.get( i ).setAmount( amount );
                return true;
            }
        }
        ingredients.add( new Ingredient( name, amount ) );
        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            buf.append( ingredients.get( i ).getName() + ": " + ingredients.get( i ).getAmount() + "\n" );
        }
        return buf.toString();
    }

}
