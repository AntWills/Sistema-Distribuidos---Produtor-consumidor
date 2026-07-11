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

    @PostMapping("")
    public Order create(@RequestBody CreateOrderReq req) {
        return orderService.createOrder(req.item_id);
    }
}
