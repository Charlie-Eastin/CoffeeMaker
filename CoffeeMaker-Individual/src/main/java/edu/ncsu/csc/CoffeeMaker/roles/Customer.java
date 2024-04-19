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

@Entity
@Table ( name = "user" )
public class Customer extends User {

    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private final List<Order> orders = new ArrayList<>();

    @Min ( 0 )
    private Integer           money;

    public void setMoney ( final Integer money ) {
        this.money = money;
    }

    public Integer getMoney () {
        return this.money;
    }

    @Override
    public void setId ( final long id ) {
        super.setId( id );
    }

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

    public boolean addOrder ( final int money, final Recipe recipe ) {
        if ( recipe.getPrice() <= money ) {
            final Order order = new Order( recipe, this );
            this.money -= recipe.getPrice();
            this.orders.add( order );
            return true;
        }
        return false;

    }

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
