package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.roles.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    /**
     * Finds an Ingredient object with the provided name. Spring will generate
     * code to make this happen.
     *
     * @param name
     *            Name of the recipe
     * @return Found Ingredient, null if none.
     */
    Staff findByName ( String name );

}
