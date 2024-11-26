package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.menuItem.MenuItem;
import com.pwr.gastromate.data.order.Order;
import com.pwr.gastromate.data.order.OrderItem;
import com.pwr.gastromate.data.order.OrderItemId;
import com.pwr.gastromate.dto.order.OrderItemDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    // Konwersja z encji OrderItem do DTO
    public static OrderItemDTO toDto(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getMenuItem().getId(),
                orderItem.getMenuItem().getName(),
                orderItem.getOrder().getId(),
                orderItem.getQuantity(),
                orderItem.getPriceAtOrder()
        );
    }

    // Konwersja z DTO do encji OrderItem
    public static OrderItem toEntity(OrderItemDTO orderItemDTO, MenuItem menuItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(new OrderItemId(orderItemDTO.getOrderId(), orderItemDTO.getMenuItemId()));
        orderItem.setMenuItem(menuItem);
        orderItem.setOrder(order);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPriceAtOrder(orderItemDTO.getPriceAtOrder());
        return orderItem;
    }
}
