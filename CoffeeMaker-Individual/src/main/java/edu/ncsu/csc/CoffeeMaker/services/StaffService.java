package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;
import edu.ncsu.csc.CoffeeMaker.roles.Staff;

@Component
@Transactional
public class StaffService extends Service<Staff, Long> {

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private StaffRepository staffRepository;

    @Override
    protected JpaRepository<Staff, Long> getRepository () {
        return staffRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Staff findByName ( final String name ) {
        return staffRepository.findByName( name );
    }

}
