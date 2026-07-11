package com.sistemas_distribuidos.lanchonete.orders;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    private OrderService orderService;

    public OrderController (OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public List<Order> listOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public Order findById(@PathVariable Integer id) {
        return orderService.getById(id);
    }

    @PostMapping("")
    public Order create(@RequestBody CreateOrderReq req) {
        return orderService.createOrder(req.itemId());
    }

    @PatchMapping("/{id}/next")
    public Order update(@PathVariable Integer id) {
        return this.orderService.nextStatus(id);
    }
}
