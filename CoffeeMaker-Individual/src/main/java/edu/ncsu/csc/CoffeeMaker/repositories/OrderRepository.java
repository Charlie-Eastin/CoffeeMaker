package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds an Order object with the provided name. Spring will generate code
     * to make this happen.
     *
     * @param name
     *            Name of the order
     * @return Found Order, null if none.
     */
    Order findByName ( String name );

}
