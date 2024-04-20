package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * A model for the Customer entity in the CoffeeMaker. Represents the access and
 * properties that a customer using the CoffeeMaker system would have
 */
@Entity
@Table ( name = "user" )
public class Customer extends User {

    /**
     * A list keeping track of all the orders associated with this customer
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private final List<Order> orders = new ArrayList<>();

    /**
     * The amount of money that this customer has
     */
    @Min ( 0 )
    private Integer           money;

    /**
     * Setter for money
     *
     * @param money
     *            amount of money to set
     */
    public void setMoney ( final Integer money ) {
        this.money = money;
    }

    /**
     * Getter for money
     *
     * @return amount of money the customer has
     */
    public Integer getMoney () {
        return this.money;
    }

    @Override
    public void setId ( final long id ) {
        super.setId( id );
    }

    /**
     * Getter for orders list
     *
     * @return list of orders
     */
    public List<Order> getOrders () {
        return orders;
    }

    /**
     * Get the ID of the user
     *
     * @return the ID
     */
    @Override
    @JsonProperty ( "id" )
    public Long getId () {
        return super.getId();
    }

    /**
     * Adds an order to the customer's orders and subtracts the appropriate
     * amount of money from the customer's total money
     *
     * @param money
     *            how much money the customer has
     * @param recipe
     *            the recipe associated with an order to add
     * @return true if the order can successfully be made, otherwise false
     */
    public boolean addOrder ( final int money, final Recipe recipe ) {
        if ( recipe.getPrice() <= money ) {
            final Order order = new Order( recipe );
            this.money -= recipe.getPrice();
            this.orders.add( order );
            return true;
        }
        return false;

    }

    /**
     * Removes an order from this customer's order list
     *
     * @param orderId
     *            the ID of the order to remove
     * @return true if the order was successfully removed, otherwise false
     */
    public boolean pickupOrder ( final Long orderId ) {
        for ( int i = 0; i < orders.size(); i++ ) {
            final Order order = orders.get( i );
            if ( order.getId() == orderId ) {
                orders.remove( i );
                return true;
            }
        }
        return false;

    }

}
