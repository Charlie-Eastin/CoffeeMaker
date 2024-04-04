package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIStaffTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    @Autowired
    private InventoryService      inventoryService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void testInventoryAPI () throws Exception {

        final List<Ingredient> list = new LinkedList<Ingredient>();

        list.add( new Ingredient( "Milk", 5 ) );
        list.add( new Ingredient( "Coffee", 4 ) );
        list.add( new Ingredient( "Tea", 2 ) );
        list.add( new Ingredient( "Sugar", 1 ) );

        final Inventory inventory = new Inventory( list );

        final List<Ingredient> emptyList = new LinkedList<Ingredient>();
        final Inventory inventory2 = new Inventory( emptyList );

        // Test with populated list.
        mvc.perform( put( "/api/v1/staff/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory.getIngredients() ) ) ).andExpect( status().isOk() );

        assertEquals( 1, (int) inventoryService.count() );

        // Check inventory amounts
        assertEquals( inventoryService.getInventory().getIngredients().size(), 4 );
        assertEquals( inventoryService.getInventory().getIngredients().get( 0 ).getName(), "Milk" );
        assertEquals( inventoryService.getInventory().getIngredients().get( 1 ).getName(), "Coffee" );
        assertEquals( inventoryService.getInventory().getIngredients().get( 2 ).getName(), "Tea" );
        assertEquals( inventoryService.getInventory().getIngredients().get( 3 ).getName(), "Sugar" );

        // Try it again with a list of zero ingredients.
        mvc.perform( put( "/api/v1/staff/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory2.getIngredients() ) ) ).andExpect( status().isOk() );

        // Inventory should still be there.
        assertEquals( 1, (int) inventoryService.count() );
        // But should have no ingredients.
        assertEquals( inventoryService.getInventory().getIngredients().size(), 0 );

    }

}
