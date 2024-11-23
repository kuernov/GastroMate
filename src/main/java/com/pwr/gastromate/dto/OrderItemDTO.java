package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Integer menuItemId;
    private String menuItemName;
    private Integer orderId;
    private int quantity;
    private BigDecimal priceAtOrder;
}
