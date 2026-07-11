package com.sistemas_distribuidos.lanchonete.orders;


import com.fasterxml.jackson.annotation.JsonProperty;

public record InvoicingReq(
        @JsonProperty("item_id")
        Integer itemId,
        Float price
) {
}
