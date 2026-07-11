package com.sistemas_distribuidos.lanchonete.orders;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {
    private List<Order> orders;

    public OrderService() {
        orders = new ArrayList<>();
    }

    public Order createOrder(Integer itemId) {
        Random random = new Random();

        Order order = new Order(
                random.nextInt(1, 1_000_000),
                OrderStatus.PREPARANDO
        );

        orders.add(order);

        return order;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
