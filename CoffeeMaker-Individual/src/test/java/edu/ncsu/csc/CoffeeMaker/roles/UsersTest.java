package edu.ncsu.csc.CoffeeMaker.roles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UsersTest {

    @Autowired
    private StaffService    staffService;
    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void setup () {
        customerService.deleteAll();
        staffService.deleteAll();
    }

    @Test
    @Transactional
    public void testParametersStaff () {
        final Staff staff = new Staff();
        staff.setName( "John" );
        staff.setType( "STAFF" );
        staff.setId( 1 );

        staffService.save( staff );

        Assertions.assertEquals( "John", staff.getName() );
        Assertions.assertEquals( "STAFF", staff.getType() );
        Assertions.assertEquals( 1, staff.getId() );

    }

    @Test
    @Transactional
    public void testParametersCustomer () {
        final Customer customer = new Customer();
        customer.setName( "John" );
        customer.setType( "CUSTOMER" );
        customer.setMoney( 5 );
        customer.setId( 2 );

        customerService.save( customer );

        Assertions.assertEquals( "John", customer.getName() );
        Assertions.assertEquals( "CUSTOMER", customer.getType() );
        Assertions.assertEquals( 5, customer.getMoney() );
        Assertions.assertEquals( 2, customer.getId() );

    }

    @Test
    @Transactional
    public void testParametersUser () {
        final User user = new User();
        user.setName( "John" );
        user.setType( "USER" );
        // user.setId( 3 );

        Assertions.assertEquals( "John", user.getName() );
        Assertions.assertEquals( "USER", user.getType() );
        // Assertions.assertEquals( 3, user.getId() );

    }

}
