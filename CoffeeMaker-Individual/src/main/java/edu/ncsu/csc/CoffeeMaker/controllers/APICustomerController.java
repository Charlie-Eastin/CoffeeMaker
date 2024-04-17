package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for the Customers
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {
    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe models
     */
    @Autowired
    private RecipeService    recipeService;

    /**
     * CustomerService object, to be autowired in by Spring to allow for
     * manipulating the Customer model
     */
    @Autowired
    private CustomerService  customerService;

    /**
     * Updates a user's money amount to the given amount of money
     *
     * @param name
     *            The username of the user who's money is being updated
     * @param money
     *            The amount of money to update to
     * @return ResponseEntity indicating whether the money can be successfully
     *         edited or not
     * @throws Exception
     *             if the money cannot be updated
     */
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
            return new ResponseEntity( errorResponse( "Money could not be updated" ), HttpStatus.CONFLICT );
        }
    }

    /**
     * Gets the current user's username
     *
     * @return ResponseEntity with the username and whether the username was
     *         able to be retrieved
     */
    @GetMapping ( BASE_PATH + "/username" )
    public ResponseEntity getUserName () {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ( principal instanceof UserDetails ) {
            final String username = ( (UserDetails) principal ).getUsername();
            final Gson gson = new Gson();
            return new ResponseEntity( gson.toJson( username ), HttpStatus.OK );
        }
        else {
            final String username = principal.toString();
            final Gson gson = new Gson();
            return new ResponseEntity( gson.toJson( username ), HttpStatus.OK );
        }
    }

    /**
     * Gets the role of the current user
     *
     * @return the role and whether the role was able to be retrieved
     */
    @GetMapping ( BASE_PATH + "/role" )
    public ResponseEntity getUserRole () {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Gson gson = new Gson();

        final boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch( r -> r.getAuthority().equals( "STAFF" ) );
        System.out.println( isStaff );
        if ( isStaff ) {
            return new ResponseEntity( gson.toJson( "STAFF" ), HttpStatus.OK );
        }
        return new ResponseEntity( gson.toJson( "CUSTOMER" ), HttpStatus.OK );
    }
}
