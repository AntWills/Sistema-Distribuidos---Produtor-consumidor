package com.sistemas_distribuidos.lanchonete.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.swing.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Service
public class Order {
    private Integer id;
    OrderStatus status;
}
