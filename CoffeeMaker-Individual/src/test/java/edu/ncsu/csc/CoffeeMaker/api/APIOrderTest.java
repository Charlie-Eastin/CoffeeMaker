package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.roles.Customer;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class APIOrderTest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private OrderService service;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		service.deleteAll();
	}

	@Test
	@Transactional
	public void testOrderAPI() throws Exception {

		service.deleteAll();

		Assertions.assertEquals(0, (int) service.count());

		// create recipe for order
		final Recipe r1 = new Recipe();
		r1.setName("Black Coffee");
		r1.setPrice(1);
		assertTrue(r1.addIngredient(new Ingredient("Coffee", 1)));
		assertTrue(r1.addIngredient(new Ingredient("Milk", 0)));
		assertTrue(r1.addIngredient(new Ingredient("Sugar", 0)));
		assertTrue(r1.addIngredient(new Ingredient("Chocolate", 0)));

//		final Staff staff = new Staff();
//		staff.setName("John");
//		staff.setType("STAFF");
//		staff.setId(1);

		final Customer customer = new Customer();
		customer.setName("John");
		customer.setType("CUSTOMER");
		customer.setMoney(5);
		customer.setId(2);

		final Order order = new Order(r1, customer);

		Assertions.assertEquals(0, (int) service.count());

		mvc.perform(
				post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(order)))
				.andExpect(status().isOk());

		Assertions.assertEquals(1, (int) service.count());

	}

}
