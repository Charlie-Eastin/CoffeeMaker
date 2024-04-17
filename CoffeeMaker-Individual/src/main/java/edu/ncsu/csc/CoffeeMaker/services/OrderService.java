package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

@Component
@Transactional
public class OrderService extends Service<Order, Long> {

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public List<Order> getOrders () {
        return orderRepository.findAll();
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Optional<Order> getOrder ( final long id ) {
        return orderRepository.findById( id );
    }

}
