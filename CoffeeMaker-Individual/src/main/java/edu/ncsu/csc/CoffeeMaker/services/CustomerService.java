package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
import edu.ncsu.csc.CoffeeMaker.roles.Customer;

/**
 * Used to handle CRUD operations to a Customer.
 */
@Component
@Transactional
public class CustomerService extends Service<Customer, Long> {

    /**
     * CustomerRepository, to be autowired in by Spring and provide CRUD
     * operations on Customer model.
     */
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected JpaRepository<Customer, Long> getRepository () {
        return customerRepository;
    }

    /**
     * Find a customer with the provided name
     *
     * @param name
     *            Name of the customer to find
     * @return found customer, null if none
     */
    public Customer findByName ( final String name ) {
        return customerRepository.findByName( name );
    }

}
