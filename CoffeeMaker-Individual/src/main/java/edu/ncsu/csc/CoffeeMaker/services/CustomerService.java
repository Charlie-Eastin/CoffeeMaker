package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
import edu.ncsu.csc.CoffeeMaker.roles.Customer;

@Component
@Transactional
public class CustomerService extends Service<Customer, Long> {

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected JpaRepository<Customer, Long> getRepository () {
        return customerRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Customer findByName ( final String name ) {
        return customerRepository.findByName( name );
    }

}
