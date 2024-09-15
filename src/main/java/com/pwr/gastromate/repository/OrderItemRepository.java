package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.data.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
