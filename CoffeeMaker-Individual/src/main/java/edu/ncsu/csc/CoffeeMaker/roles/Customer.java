package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@Entity
public class Customer extends User {

    @Id
    @GeneratedValue
    private Long        id;

    private String      name;

    private String      type;

    private int         money;

    private List<Order> orders;

    public void setMoney ( final int money ) {
        this.money = money;
    }

    public int getMoney () {
        return this.money;
    }

    @Override
    public void setName ( final String name ) {
        this.name = name;
    }

    @Override
    public String getName () {
        return this.name;
    }

    @Override
    public void setType ( final String type ) {
        this.type = type;
    }

    @Override
    public String getType () {
        return this.type;
    }

    @Override
    public void setId ( final long id ) {
        this.id = id;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
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

    public boolean pickupOrder ( final long orderId ) {
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
