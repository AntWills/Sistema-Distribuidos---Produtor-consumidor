package com.sistemas_distribuidos.lanchonete;

public record Order (
        Integer id,
        OrderStatus status
) {
}
