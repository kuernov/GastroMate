package com.pwr.gastromate.dto;

import com.pwr.gastromate.data.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SupplyOrderDTO {
    private Integer id;
    private LocalDateTime orderDate;
    private DeliveryStatus status;
    private List<SupplyOrderItemDTO> orderItems;

}
