package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.roles.Customer;

/**
 * CustomerRepository is used to provide CRUD operations for the Customer model.
 * Spring will generate appropriate code with JPA.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Finds a Customer object with the provided name. Spring will generate code
     * to make this happen.
     *
     * @param name
     *            Name of the customer
     * @return Found Customer, null if none.
     */
    Customer findByName ( String name );

}
