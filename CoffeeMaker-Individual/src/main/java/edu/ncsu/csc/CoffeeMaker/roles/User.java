package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Order;

@Entity
public class User extends DomainObject {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String type;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

}
