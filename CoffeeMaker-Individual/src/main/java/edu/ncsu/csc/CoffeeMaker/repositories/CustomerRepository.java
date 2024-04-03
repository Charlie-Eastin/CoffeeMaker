package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.roles.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Finds an Ingredient object with the provided name. Spring will generate
     * code to make this happen.
     *
     * @param name
     *            Name of the recipe
     * @return Found Ingredient, null if none.
     */
    Customer findByName ( String name );

}
