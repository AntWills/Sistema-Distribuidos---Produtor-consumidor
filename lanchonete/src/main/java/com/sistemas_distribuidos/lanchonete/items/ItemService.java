package com.sistemas_distribuidos.lanchonete;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class ItemService {
    private List<Item> items;
    private List<Order> orders;

    public ItemService () {
        orders = new ArrayList<>();
        items = new LinkedList<>();

        items.add(new Item(
                1,
                "Caldo de Peixe",
                35F));
        items.add(new Item(
                2,
                "Salgado de Queijo",
                15F));
        items.add(new Item(
                3,
                "Bolo de Cenoura",
                10F));
    }

    public Order createOrder(Integer itemId) {
        Random random = new Random();

        Order order = new Order(
                random.nextInt(),
                OrderStatus.PREPARANDO
        );

        orders.add(order);

        return order;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
