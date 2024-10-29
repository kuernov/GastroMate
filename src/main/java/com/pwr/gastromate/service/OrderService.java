package com.pwr.gastromate.service;

import com.pwr.gastromate.data.MenuItem;
import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.OrderDTO;
import com.pwr.gastromate.dto.OrderItemDTO;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.OrderItemRepository;
import com.pwr.gastromate.repository.OrderRepository;
import com.pwr.gastromate.service.mapper.OrderItemMapper;
import com.pwr.gastromate.service.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    public OrderService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }


    @Transactional
    public Order createOrder(List<OrderItemDTO> itemDTOList, User user) {
        Order order = new Order();
        order.setUser(user);
        List<OrderItem> itemList = new ArrayList<>();
        for (OrderItemDTO itemDTO : itemDTOList) {
            int menuItemId = itemDTO.getMenuItemId();

            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));

            OrderItem orderItem = OrderItemMapper.toEntity(itemDTO, menuItem, order);
            itemList.add(orderItem);
        }

        order.setOrderItems(itemList);
        orderRepository.save(order);
        orderItemRepository.saveAll(itemList);

        return order;
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    public Page<OrderDTO> getAllOrdersForUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepository.findByUser(user, pageable);

        List<OrderDTO> orderDTOs = ordersPage.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDTOs, pageable, ordersPage.getTotalElements());
    }

    public Page<Order> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public void deleteOrder(int orderId) {
        Order order = getOrderById(orderId);
        orderRepository.delete(order);
    }

    public BigDecimal calculateTotalPrice(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem -> orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
