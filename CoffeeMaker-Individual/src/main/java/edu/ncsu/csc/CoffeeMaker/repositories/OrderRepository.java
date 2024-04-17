package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * OrderRepository is used to provide CRUD operations for the Order model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Grant Benson
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 *
	 * @return the list of orders in repository
	 */
	List<Order> getOrders();

}
