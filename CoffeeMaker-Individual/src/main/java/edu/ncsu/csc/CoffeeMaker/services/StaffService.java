package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;
import edu.ncsu.csc.CoffeeMaker.roles.Staff;

/**
 * Used to handle CRUD operations on the Staff model
 */
@Component
@Transactional
public class StaffService extends Service<Staff, Long> {

    /**
     * StaffRepository, to be autowired in by Spring and provide CRUD operations
     * on Staff model.
     */
    @Autowired
    private StaffRepository staffRepository;

    @Override
    protected JpaRepository<Staff, Long> getRepository () {
        return staffRepository;
    }

    /**
     * Find an staff with the provided name
     *
     * @param name
     *            Name of the staff to find
     * @return found staff, null if none
     */
    public Staff findByName ( final String name ) {
        return staffRepository.findByName( name );
    }

}
