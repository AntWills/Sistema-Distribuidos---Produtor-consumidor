package com.sistemas_distribuidos.lanchonete;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemController {
    private ItemService itemService;

    public ItemController () {
        itemService = new ItemService();
    }

    @GetMapping("")
    public List<Item> listItems() {
        return itemService.getItems();
    }
}
