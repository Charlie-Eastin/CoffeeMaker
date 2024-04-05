package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
