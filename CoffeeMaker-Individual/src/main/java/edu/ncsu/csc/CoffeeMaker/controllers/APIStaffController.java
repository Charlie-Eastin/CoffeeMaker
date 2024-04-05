package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIStaffController extends APIController {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private RecipeService    recipeService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the Inventory to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/staff/inventory" )
    public ResponseEntity getInventory () {
        final Inventory inventory = inventoryService.getInventory();
        return new ResponseEntity( inventory, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/staff/inventory" )
    public ResponseEntity updateInventory ( @RequestBody final Inventory inventory ) {
        final Inventory inventoryCurrent = inventoryService.getInventory();
        final List<Ingredient> list = inventory.getIngredients();
        try {
            inventoryCurrent.setIngredients( list );
        }
        catch ( final IllegalArgumentException e ) {
            return new ResponseEntity( errorResponse( "Duplicate ingredients not allowed" ), HttpStatus.CONFLICT );
        }

        inventoryService.save( inventoryCurrent );
        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }

    /**
     * REST API method for editing a recipe. Deletes the current recipe and
     * replaces it with one sharing the same name.
     *
     * @param name
     *            the current name of the recipe to be changed.
     * @param recipe
     *            the new recipe to be instantiated into the database
     * @return a Response to the request.
     */
    @PostMapping ( BASE_PATH + "/staff/recipes/{name}" )
    public ResponseEntity editRecipe ( @PathVariable ( "name" ) final String name, @RequestBody final Recipe recipe ) {
        if ( recipeService.findByName( name ) == null ) {
            return new ResponseEntity( errorResponse( "Recipe not found " + recipe.getName() ), HttpStatus.CONFLICT );
        }
        if ( recipe.getIngredients().size() == 0 ) {
            return new ResponseEntity( errorResponse( "Recipe cannot have ingredients size of 0" ),
                    HttpStatus.CONFLICT );
        }
        if ( !recipe.noDuplicates() ) {
            return new ResponseEntity( errorResponse( "Recipe cannot have duplicate ingredients" ),
                    HttpStatus.CONFLICT );
        }
        final Recipe recipe2 = recipeService.findByName( name );
        recipeService.delete( recipe2 );
        recipeService.save( recipe );
        return new ResponseEntity( successResponse( recipe.getName() + " successfully edited" ), HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/staff/recipes" )
    public ResponseEntity createRecipe ( @RequestBody final Recipe recipe ) {
        if ( null != recipeService.findByName( recipe.getName() ) ) {
            return new ResponseEntity( errorResponse( "Recipe with the name " + recipe.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( recipeService.findAll().size() < 3 && recipe.getIngredients().size() > 0 && recipe.noDuplicates() ) {
            recipeService.save( recipe );
            return new ResponseEntity( successResponse( recipe.getName() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse( "Insufficient space in recipe book for recipe " + recipe.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/staff/recipes/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final Recipe recipe = recipeService.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        recipeService.delete( recipe );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

}
