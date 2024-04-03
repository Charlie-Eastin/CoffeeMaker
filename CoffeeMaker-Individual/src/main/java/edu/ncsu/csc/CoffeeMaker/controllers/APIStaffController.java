package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
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
    private InventoryService inventoryService;
    private RecipeService    recipeService;

    @PutMapping ( BASE_PATH + "/staff/inventory" )
    public boolean updateInventory ( @RequestBody final List<Ingredient> ingredientList ) {
        try {
            final Inventory inventory = inventoryService.getInventory();
            inventory.setIngredients( ingredientList );
            inventoryService.save( inventory );
        }
        catch ( final Exception e ) {
            return false;
        }

        return true;
    }

    @PostMapping ( BASE_PATH + "/staff/recipes/{name}" )
    public boolean editRecipe ( @PathVariable ( "name" ) final String name, @RequestBody final int price,
            @RequestBody final List<Ingredient> ingredientList ) {
        try {
            final Recipe recipe = recipeService.findByName( name );
            final Recipe newRecipe = new Recipe();
            for ( int i = 0; i < ingredientList.size(); i++ ) {
                newRecipe.addIngredient( ingredientList.get( i ) );
            }
            newRecipe.setName( name );
            newRecipe.setPrice( price );
            recipeService.delete( recipe );
            recipeService.save( newRecipe );

        }
        catch ( final Exception e ) {
            return false;
        }
        return true;
    }

    @DeleteMapping ( BASE_PATH + "/staff/recipes/{name}" )
    public boolean deleteRecipe ( @PathVariable ( "name" ) final String name ) {
        try {
            final Recipe recipe = recipeService.findByName( name );
            recipeService.delete( recipe );
            return true;
        }
        catch ( final Exception e ) {
            return false;
        }

    }

}
