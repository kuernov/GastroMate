package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.dto.OrderDTO;
import com.pwr.gastromate.dto.OrderItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalOrderPrice());

        // Mapowanie elementów OrderItem na OrderItemDTO
        if (order.getOrderItems() != null) {
            orderDTO.setOrderItems(order.getOrderItems().stream()
                    .map(this::toOrderItemDTO)
                    .collect(Collectors.toList()));

            // Obliczenie całkowitej ceny zamówienia
            BigDecimal totalPrice = calculateTotalPrice(order);
            orderDTO.setTotalPrice(totalPrice);
        }

        return orderDTO;
    }

    private OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setMenuItemId(orderItem.getMenuItem().getId());
        orderItemDTO.setMenuItemName(orderItem.getMenuItem().getName());
        orderItemDTO.setOrderId(orderItem.getOrder().getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPriceAtOrder(orderItem.getPriceAtOrder());

        return orderItemDTO;
    }

    // Metoda do obliczenia całkowitej ceny zamówienia
    private BigDecimal calculateTotalPrice(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem -> orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
