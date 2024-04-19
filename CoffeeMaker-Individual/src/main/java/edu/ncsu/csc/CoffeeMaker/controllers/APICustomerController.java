package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {
    @Autowired
    private OrderService    orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RecipeService   recipeService;

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

    @GetMapping ( BASE_PATH + "/customers/users/{name}" )
    public ResponseEntity getCustomer ( @PathVariable ( "name" ) final String name ) throws Exception {
        final Customer user = customerService.findByName( name );
        return new ResponseEntity( user, HttpStatus.OK );
    }

    @DeleteMapping ( BASE_PATH + "/orders/{id}/{name}" )
    public ResponseEntity pickupOrder ( @PathVariable ( "id" ) final String stringId,
            @PathVariable ( "name" ) final String customerName ) {
        try {
            final Long id = Long.parseLong( stringId );
            final Order order = orderService.findById( id );
            final Customer customer = customerService.findByName( customerName );
            if ( customer.pickupOrder( id ) != true ) {
                return new ResponseEntity( errorResponse( "Cannot pickup order" ), HttpStatus.CONFLICT );
            }
            customerService.save( customer );
            orderService.delete( order );
            return new ResponseEntity( successResponse( customer.getName() + "'s order was successfully picked up" ),
                    HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Cannot pickup order | Something went wrong" ),
                    HttpStatus.CONFLICT );
        }
    }

    @PostMapping ( BASE_PATH + "/{name}/orders" )
    public ResponseEntity addOrder ( @PathVariable ( "name" ) final String name, @RequestBody final Recipe recipe ) {
        try {
            Customer customer = customerService.findByName( name );
            if ( customer == null ) {
                customer = new Customer();
                customer.setName( name );
                customer.setType( "CUSTOMER" );
                customer.setMoney( 100 );
            }

            // final Recipe recipe = recipeService.findById(
            // order.getRecipe().getId() );
            if ( customer.addOrder( customer.getMoney(), recipe ) != true ) {
                return new ResponseEntity( errorResponse( "Customer does not have enough money" ),
                        HttpStatus.CONFLICT );
            }
            customerService.save( customer );
            return new ResponseEntity<String>( successResponse( String.valueOf( customer.getMoney() ) ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            System.out.println( e.getMessage() );
            return new ResponseEntity( errorResponse( "Unable to make order" ), HttpStatus.CONFLICT );
        }
    }

}
