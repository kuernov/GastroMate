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
import com.pwr.gastromate.specification.OrderSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Page<OrderDTO> getAllOrdersForUser(User user, int page, int size, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page, size);

        // Create a specification with optional filters
        Specification<Order> spec = Specification.where(OrderSpecification.belongsToUser(user));

        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay(); // Start of day
            spec = spec.and(OrderSpecification.hasOrderDateAfter(startDateTime));
        }

        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // End of day
            spec = spec.and(OrderSpecification.hasOrderDateBefore(endDateTime));
        }

        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);

        return ordersPage.map(orderMapper::toDTO);
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


    @PersistenceContext
    private EntityManager entityManager;

    public void testIngredientUsageQuery(Integer ingredientId) {
        List<Object[]> results = entityManager.createQuery(
                        "SELECT DATE(o.orderDate), i.id, i.name, SUM(oi.quantity * mii.quantityRequired) " +
                                "FROM Order o " +
                                "JOIN o.orderItems oi " +
                                "JOIN oi.menuItem mi " +
                                "JOIN mi.menuItemIngredients mii " +
                                "JOIN mii.ingredient i " +
                                "WHERE i.id = :ingredientId " +
                                "GROUP BY DATE(o.orderDate), i.id, i.name " +
                                "ORDER BY DATE(o.orderDate), i.name"
                )
                .setParameter("ingredientId", ingredientId)
                .getResultList();

        for (Object[] row : results) {
            System.out.println("orderDate type: " + (row[0] != null ? row[0].getClass().getName() : "null"));
            System.out.println("ingredientId type: " + (row[1] != null ? row[1].getClass().getName() : "null"));
            System.out.println("ingredientName type: " + (row[2] != null ? row[2].getClass().getName() : "null"));
            System.out.println("dailyConsumption type: " + (row[3] != null ? row[3].getClass().getName() : "null"));
        }
    }

}
