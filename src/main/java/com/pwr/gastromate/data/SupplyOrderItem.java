package com.pwr.gastromate.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

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

    private int quantityOrdered;
    private BigDecimal priceAtOrder;

}
