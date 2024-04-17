package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
	// query that selects all instances of the Order entity from the database
	@Query("SELECT o FROM Order o")
	List<Order> getOrders();

//	/**
//	 *
//	 * @param user affiliated with order
//	 * @return list of orders associated with user
//	 */
//	List<Order> findByUser(User user);

}
