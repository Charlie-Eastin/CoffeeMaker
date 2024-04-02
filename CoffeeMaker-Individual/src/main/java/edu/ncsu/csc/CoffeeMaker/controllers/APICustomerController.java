package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {
    private String           name;
    private String           password;
    private String           type;
    private InventoryService inventoryService;
    private RecipeService    recipeService;
    private UserService      userService;

    @PutMapping ( BASE_PATH + "/users/{name}" )
    public boolean updateMoney ( @PathVariable ( "name" ) final String name, @RequestBody final int money ) {
        try {
            final Customer user = (Customer) userService.findByName( name );
            user.setMoney( money );
            userService.save( user );
        }
        catch ( final Exception e ) {
            return false;
        }

        return true;
    }
}
