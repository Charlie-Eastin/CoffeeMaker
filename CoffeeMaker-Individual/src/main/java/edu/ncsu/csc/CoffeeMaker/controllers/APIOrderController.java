package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
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
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class APIOrderController extends APIController {

	/**
	 * OrderService object, to be autowired in by Spring to allow for manipulating
	 * the Order model
	 */
	@Autowired
	private OrderService service;

	/**
	 * REST API method to provide GET access to all orders in the system
	 *
	 * @return JSON representation of all orders
	 */
	@GetMapping(BASE_PATH + "/orders")
	public List<Order> getOrders() {
		return service.findAll();
	}

	/**
	 * REST API method to provide POST access to the Order model. This is used to
	 * create a new Order by automatically converting the JSON RequestBody provided
	 * to a order object. Invalid JSON will fail.
	 *
	 * @param money  is the integer value payed
	 * @param recipe is the valid Recipe to be saved to order
	 * @return ResponseEntity indicating success if the Order could be saved or an
	 *         error if it could not be
	 */
	@PostMapping(BASE_PATH + "/orders")
	public ResponseEntity addOrder(int money, Recipe recipe) {

//		if () {
//            return new ResponseEntity( errorResponse( "ordererrmsg" ), HttpStatus.CONFLICT );
//        }
		// return success OK 200
		// want to call makecoffee to check for change/viability
		// makeCoffee();

		return new ResponseEntity(successResponse("Order was successfully created"), HttpStatus.OK);

	}

	@PutMapping(BASE_PATH + "/orders/{id}")
	public ResponseEntity fufillOrder(Order order) {
		// if status is picked up then delete from repository?

		return new ResponseEntity(successResponse("Order status changed"), HttpStatus.OK);

	}

}
