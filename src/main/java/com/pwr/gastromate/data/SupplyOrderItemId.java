package com.pwr.gastromate.data;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class SupplyOrderItemId implements Serializable {
    private Integer supplyOrderId;
    private Integer ingredientId;
}
