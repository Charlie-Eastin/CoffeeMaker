package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {
    private InventoryService inventoryService;
    private RecipeService    recipeService;
    private CustomerService  customerService;

    @PutMapping ( BASE_PATH + "/customers/users/{name}" )
    public boolean updateMoney ( @PathVariable ( "name" ) final String name, @RequestBody final int money ) {
        try {
            final Customer user = customerService.findByName( name );
            user.setMoney( money );
            customerService.save( user );
        }
        catch ( final Exception e ) {
            return false;
        }

        return true;
    }
}
