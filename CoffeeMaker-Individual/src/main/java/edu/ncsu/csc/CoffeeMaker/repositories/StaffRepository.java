package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.roles.Staff;

/**
 * StaffRepository is used to provide CRUD operations for the Staff model.
 * Spring will generate appropriate code with JPA.
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {
    /**
     * Finds a Staff object with the provided name. Spring will generate code to
     * make this happen.
     *
     * @param name
     *            Name of the staff
     * @return Found Staff, null if none.
     */
    Staff findByName ( String name );

}
