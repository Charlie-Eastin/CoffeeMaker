package edu.ncsu.csc.CoffeeMaker.roles;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import edu.ncsu.csc.CoffeeMaker.models.Order;

@Entity
public class Staff extends User {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String type;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Get the ID of the Recipe
	 *
	 * @return the ID
	 */
	@Override
	public Long getId() {
		return id;
	}

}
