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
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UsersTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {

    }

    @Test
    @Transactional
    public void testParametersStaff () {
        final Staff staff = new Staff();
        staff.setName( "John" );
        staff.setType( "STAFF" );

        Assertions.assertEquals( "John", staff.getName() );
        Assertions.assertEquals( "STAFF", staff.getType() );

    }

    @Test
    @Transactional
    public void testParametersCustomer () {
        final Customer customer = new Customer();
        customer.setName( "John" );
        customer.setType( "CUSTOMER" );
        customer.setMoney( 5 );

        Assertions.assertEquals( "John", customer.getName() );
        Assertions.assertEquals( "CUSTOMER", customer.getType() );
        Assertions.assertEquals( 5, customer.getMoney() );

    }

    @Test
    @Transactional
    public void testParametersUser () {
        final User user = new User();
        user.setName( "John" );
        user.setType( "USER" );

        Assertions.assertEquals( "John", user.getName() );
        Assertions.assertEquals( "USER", user.getType() );

    }

}
