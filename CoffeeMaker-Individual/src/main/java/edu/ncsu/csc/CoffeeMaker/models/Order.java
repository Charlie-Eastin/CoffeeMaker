package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.roles.User;

@Entity
public class Order extends DomainObject {
	@Id
	@GeneratedValue
	private Long id;

	private User user;

	private Recipe recipe;

	private String status;

	public Order() {

	}

	public Order(Recipe recipe, User user) {
		this.recipe = recipe;
		this.user = user;
	}

//	/**
//	 * @param user
//	 * @param recipe
//	 * @param status
//	 */
//	public Order(User user, Recipe recipe, String status) {
//		this.user = user;
//		this.recipe = recipe;
//		this.status = status;
//	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the recipe
	 */
	public Recipe getRecipe() {
		return recipe;
	}

	/**
	 * @param recipe the recipe to set
	 */
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get the ID of the Order
	 *
	 * @return the ID
	 */
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", user=" + user.toString() + ", recipe=" + recipe.toString() + ", status="
				+ status + "]";
	}

}
