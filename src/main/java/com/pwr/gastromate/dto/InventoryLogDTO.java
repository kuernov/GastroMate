package com.pwr.gastromate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryLogDTO {
    private Integer ingredientId;
    private String ingredientName;
    private String unitName;
    private Integer unitId;
    private String changeType;
    private BigDecimal quantityChange;
    private LocalDateTime timestamp;

}
