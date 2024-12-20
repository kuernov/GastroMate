package com.pwr.gastromate.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice; // pole na całkowitą cenę
    // Gettery i settery
}
