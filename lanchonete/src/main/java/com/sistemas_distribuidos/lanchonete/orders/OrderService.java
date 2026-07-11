package com.sistemas_distribuidos.lanchonete.orders;

import com.sistemas_distribuidos.lanchonete.items.Item;
import com.sistemas_distribuidos.lanchonete.items.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class OrderService {
    private List<Order> orders;

    @Value("${url.faturamento}")
    private String urlFaturamento;

    private ItemService itemService;

    private final RestClient restClient;

    public OrderService(ItemService itemService) {
        this.restClient = RestClient.create();
        this.itemService = itemService;

        orders = new ArrayList<>();
    }

    public Order createOrder(Integer itemId) {
        Random random = new Random();

        Order order = new Order(
                random.nextInt(1, 1_000_000),
                OrderStatus.PREPARANDO
        );

        orders.add(order);

        log.info("Dados da URL: {}\nID: {}", urlFaturamento, itemId);

        Float price = itemService.getItems().stream()
                .filter(i -> i.id().equals(itemId))
                .findFirst()
                .map(Item::price)
                .orElseThrow(() -> new IllegalArgumentException("Item com ID " + itemId + " não existe."));

        InvoicingReq payload = new InvoicingReq(itemId, price);

        try {
            restClient.post()
                    .uri(urlFaturamento + "/revenue")
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Faturamento notificado com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao notificar faturamento: {}", e.getMessage());
        }

        return order;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
