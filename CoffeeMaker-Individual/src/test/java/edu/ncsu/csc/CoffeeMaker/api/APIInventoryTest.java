package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIInventoryTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private InventoryService      service;

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
        final Inventory inventory2 = null;

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        Assertions.assertEquals( service.getInventory().getIngredients().get( 0 ).getName(), "Milk" );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory2 ) ) ).andExpect( status().isBadRequest() );

    }

    @Test
    @Transactional
    public void testInventoryAPI2 () throws Exception {

        final List<Ingredient> list = new LinkedList<Ingredient>();

        list.add( new Ingredient( "Milk", 5 ) );
        list.add( new Ingredient( "Coffee", 4 ) );
        list.add( new Ingredient( "Tea", 2 ) );
        list.add( new Ingredient( "Sugar", 1 ) );

        final Inventory inventory = new Inventory( list );

        service.save( inventory );

        Assertions.assertEquals( 1, (int) service.count() );

        Assertions.assertEquals( service.getInventory().getIngredients().get( 0 ).getName(), "Milk" );

    }

    @Test
    @Transactional
    public void testGetInventoryAPI () throws Exception {

        final List<Ingredient> list = new LinkedList<Ingredient>();

        list.add( new Ingredient( "Milk", 5 ) );
        list.add( new Ingredient( "Coffee", 4 ) );
        list.add( new Ingredient( "Tea", 2 ) );
        list.add( new Ingredient( "Sugar", 1 ) );

        final Inventory inventory = new Inventory( list );

        service.save( inventory );

        mvc.perform( get( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

    }

}
