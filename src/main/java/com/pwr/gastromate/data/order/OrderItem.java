package com.pwr.gastromate.data.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pwr.gastromate.data.menuItem.MenuItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_items")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id = new OrderItemId();

    @ManyToOne
    @MapsId("menuItemId")
    @JsonBackReference
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @ManyToOne
    @MapsId("orderId")
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

    private BigDecimal priceAtOrder;



}
