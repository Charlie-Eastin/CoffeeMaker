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

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {
    @Autowired
    private OrderService    orderService;
    @Autowired
    private CustomerService customerService;

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

    @PutMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity pickupOrder ( @PathVariable ( "id" ) final long id ) {
        try {
            final Order order = orderService.findById( id );
            final Customer customer = order.getCustomer();
            customerService.delete( customer );
            if ( customer.pickupOrder( id ) != true ) {
                customerService.save( customer ); // MIGHT BE CAUSING ISSUES WITH SAVING WRONG OBJECT
                return new ResponseEntity( errorResponse( "Cannot pickup order" ), HttpStatus.CONFLICT );

            }
            customerService.save( customer );
            orderService.delete( order );
            order.setStatus( "PICKEDUP" );
            orderService.save( order );
            return new ResponseEntity( successResponse( customer.getName() + "'s order was successfully picked up" ),
                    HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Cannot pickup order" ), HttpStatus.CONFLICT );
        }
    }

    @PutMapping ( BASE_PATH + "/orders" )
    public ResponseEntity addOrder ( @RequestBody final int money, @RequestBody final Recipe recipe,
            @RequestBody final String name ) {
        try {
            final Customer customer = customerService.findByName( name );
            final Order order = new Order( recipe, customer );
            customerService.delete( customer );
            if ( customer.addOrder( money, recipe ) != true ) {
                customerService.save( customer ); // MIGHT BE CAUSING ISSUES WITH SAVING WRONG OBJECT
                return new ResponseEntity( errorResponse( "Customer does not have enough money" ),
                        HttpStatus.CONFLICT );
            }
            orderService.save( order );
            customerService.save( customer );
            return new ResponseEntity( successResponse( customer.getName() + "'s order was successfully put in" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Cannot pickup order" ), HttpStatus.CONFLICT );
        }
    }
}
