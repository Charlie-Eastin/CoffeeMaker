package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Orders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Grant Benson
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the Order model
     */
    @Autowired
    private OrderService service;

    /**
     * REST API method to provide GET access to all orders in the system
     *
     * @return JSON representation of all orders
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<Order> getOrders () {
        return service.findAll();
    }

    // @PostMapping ( BASE_PATH + "/orders" )
    // public ResponseEntity addOrder ( final int money, @RequestBody final
    // Recipe recipe ) {
    //
    // // if () {
    // // return new ResponseEntity( errorResponse( "ordererrmsg" ),
    // // HttpStatus.CONFLICT );
    // // }
    // // return success OK 200
    // // want to call makecoffee to check for change/viability
    // // makeCoffee();
    //
    // return new ResponseEntity( successResponse( "Order was successfully
    // created" ), HttpStatus.OK );
    //
    // }

    // @PutMapping(BASE_PATH + "/orders/{id}")
    // public ResponseEntity fufillOrder(@PathVariable("id") final int id,
    // @RequestBody Order order) {
    // // if status is picked up then delete from repository?
    //
    // return new ResponseEntity(successResponse("Order status changed"),
    // HttpStatus.OK);
    //
    // }

}
