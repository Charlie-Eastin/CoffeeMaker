package edu.ncsu.csc.CoffeeMaker.roles;

public class Staff extends User {
    private String name;
    private String password;
    private int    money;
    private String type;

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
    public void setPassword ( final String password ) {
        this.password = password;
    }

    public void setMoney ( final int money ) {
        this.money = money;
    }

    public int getMoney () {
        return this.money;
    }

}
