package edu.ncsu.csc.CoffeeMaker.roles;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;

@Entity
public class User extends DomainObject {

    @Id
    @GeneratedValue
    // @JsonIgnore //ignores id for json serialize
    private Long   id;

    private String name;
    private String type;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<Order> orders = new ArrayList<>();

    public void setName ( final String name ) {
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setType ( final String type ) {
        this.type = type;
    }

    public String getType () {
        return this.type;
    }

    public void setId ( final long id ) {
        this.id = id;
    }

    @Override
    public Long getId () {
        return id;
    }

}
