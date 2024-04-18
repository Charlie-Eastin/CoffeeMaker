package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@Entity
public class Customer extends User {

    private String      name;

    private String      type;

    private List<Order> orders;
    @Min ( 0 )
    private Integer     money;

    public void setMoney ( final Integer money ) {
        this.money = money;
    }

    public Integer getMoney () {
        return this.money;
    }

    @Override
    public void setName ( final String name ) {
        super.setName( name );
    }

    @Override
    public String getName () {
        return super.getName();
    }

    @Override
    public void setType ( final String type ) {
        super.setType( type );
    }

    @Override
    public String getType () {
        return super.getType();
    }

    @Override
    public void setId ( final long id ) {
        super.setId( id );
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

    public boolean addOrder ( int money, final Recipe recipe ) {
        if ( recipe.getPrice() > money ) {
            final Order order = new Order( recipe, this );
            money -= recipe.getPrice();
            orders.add( order );
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
