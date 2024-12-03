package com.pwr.gastromate.data.supplyOrder;

import com.pwr.gastromate.data.Ingredient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "supply_order_items")
public class SupplyOrderItem {
    @EmbeddedId
    private SupplyOrderItemId id = new SupplyOrderItemId();

    @ManyToOne
    @MapsId("supplyOrderId")
    @JoinColumn(name = "supply_order_id", referencedColumnName = "id")
    private SupplyOrder supplyOrder;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    private BigDecimal quantity;

}
