package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc

public class MappingControllerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests a get request to index
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testIndex () throws Exception {
        mvc.perform( get( "/index" ) ).andExpect( status().isOk() );

    }

    /**
     * Tests a get request to addrecipe
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testAddRecipe () throws Exception {
        mvc.perform( get( "/addrecipe" ) ).andExpect( status().isOk() );

    }

    /**
     * Tests a get request to deleterecipe
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testDeleteRecipe () throws Exception {
        mvc.perform( get( "/deleterecipe" ) ).andExpect( status().isOk() );

    }

    /**
     * Tests a get request to inventory
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testInventory () throws Exception {
        mvc.perform( get( "/inventory" ) ).andExpect( status().isOk() );

    }

    /**
     * Tests a get request to makecoffee
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testMakeCoffee () throws Exception {
        mvc.perform( get( "/makecoffee" ) ).andExpect( status().isOk() );

    }

    /**
     * Tests a get request to controller
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testEditRecipe () throws Exception {
        mvc.perform( get( "/editrecipe2" ) ).andExpect( status().isOk() );

    }

}
