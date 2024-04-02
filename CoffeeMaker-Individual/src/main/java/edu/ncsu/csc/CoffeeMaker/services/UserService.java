package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;
import edu.ncsu.csc.CoffeeMaker.roles.User;

@Component
@Transactional
public class UserService extends Service<User, Long> {

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository () {
        return userRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public User findByName ( final String name ) {
        return userRepository.findByName( name );
    }

}
