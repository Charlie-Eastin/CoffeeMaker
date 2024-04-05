package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private RecipeService    recipeService;
    @Autowired
    private CustomerService  customerService;

    @PutMapping ( BASE_PATH + "/customers/users/{name}" )
    public ResponseEntity updateMoney ( @PathVariable ( "name" ) final String name, @RequestBody final int money )
            throws Exception {
        try {
            final Customer user = customerService.findByName( name );
            user.setMoney( money );
            customerService.save( user );
            return new ResponseEntity( successResponse( user.getName() + "'s money was successfully edited" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Recipe cannot have duplicate ingredients" ),
                    HttpStatus.CONFLICT );
        }
    }
}
