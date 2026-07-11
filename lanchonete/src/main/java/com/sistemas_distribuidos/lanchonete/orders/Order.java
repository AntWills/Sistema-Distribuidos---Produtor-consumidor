package com.sistemas_distribuidos.lanchonete.orders;

public record Order (
        Integer id,
        OrderStatus status
) {
}
