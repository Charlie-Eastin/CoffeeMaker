package edu.ncsu.csc.CoffeeMaker.models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
@Table ( name = "recipe" )
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /**
     * List of ingredients in the recipe
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        ingredients = new LinkedList<Ingredient>();
    }

    /**
     * Getter for the list of ingredients in the recipe
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Adds an ingredient to the list of ingredients in the recipe
     *
     * @param ingredient
     *            the ingredient to be added
     * @return true if the ingredient was successfully added, false if the new
     *         ingredient name matches an existing ingredient in the recipe
     * @throws ConstraintViolationException
     *             if the amount of the passed ingredient is less than 0
     */
    public boolean addIngredient ( final Ingredient ingredient ) {
        if ( ingredient.getAmount() < 0 ) {
            throw new ConstraintViolationException( "test", null );
        }
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( ingredient.getName() ) ) {
                return false;
            }
        }
        ingredients.add( ingredient );
        return true;
    }

    /**
     * Verifies that there aren't duplicate ingredient names in the recipe
     *
     * @return true if there aren't any duplicates, false if there are
     */
    public boolean noDuplicates () {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            for ( int j = 0; j < ingredients.size(); j++ ) {
                if ( ingredients.get( i ).getName().equals( ingredients.get( j ).getName() ) && j != i ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets the amount of an ingredient in the recipe to the amount of a passed
     * in ingredient with the same name
     *
     * @param ingredient
     *            the ingredient with the new amount information
     */
    public void setIngredient ( final Ingredient ingredient ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( ingredient.getName() ) ) {
                ingredients.get( i ).setAmount( ingredient.getAmount() );
            }
        }
    }

    /**
     * Replaces the information in this recipe with the information in another
     * passed recipe
     *
     * @param recipe
     *            recipe object with the new recipe information
     *
     * @throws NullPointerExeption
     *             if the passed recipe is null
     */
    public void editRecipe ( final Recipe recipe ) {
        if ( recipe == null ) {
            throw new NullPointerException( "Recipe cannot be null" );
        }
        this.setPrice( recipe.getPrice() );

        this.ingredients = new LinkedList<Ingredient>( recipe.getIngredients() );
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

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        String s = name + "\n";
        for ( int i = 0; i < ingredients.size(); i++ ) {
            s += ingredients.get( i ) + ",";
        }
        return s;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
