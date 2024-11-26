package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.supplyOrder.SupplyOrderItem;
import com.pwr.gastromate.data.supplyOrder.SupplyOrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyOrderItemRepository extends JpaRepository<SupplyOrderItem, SupplyOrderItemId> {
}
