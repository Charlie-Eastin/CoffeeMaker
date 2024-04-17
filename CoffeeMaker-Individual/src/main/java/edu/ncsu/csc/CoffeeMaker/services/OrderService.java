package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * The OrderService is used to handle CRUD operations on the Order model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving all orders
 *
 * @author Grant Benson
 *
 */
@Component
@Transactional
public class OrderService extends Service<Order, Long> {

	/**
	 * RecipeRepository, to be autowired in by Spring and provide CRUD operations on
	 * Recipe model.
	 */
	@Autowired
	private OrderRepository orderRepository;

	@Override
	protected JpaRepository<Order, Long> getRepository() {
		return orderRepository;
	}

	/**
	 *
	 * @return the list of orders in repository
	 */
	List<Order> getOrders() {
		return orderRepository.getOrders();
	}

}
