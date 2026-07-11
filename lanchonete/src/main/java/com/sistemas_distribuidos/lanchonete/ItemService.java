package com.sistemas_distribuidos.lanchonete;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ItemService {
    private List<Item> items;

    public ItemService () {
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

    public List<Item> getItems() {
        return items;
    }
}
