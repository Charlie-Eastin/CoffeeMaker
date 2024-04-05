package edu.ncsu.csc.CoffeeMaker.roles;

import javax.persistence.Entity;

@Entity
public class Customer extends User {

    private int money;

    public void setMoney ( final int money ) {
        this.money = money;
    }

    public int getMoney () {
        return this.money;
    }

}
