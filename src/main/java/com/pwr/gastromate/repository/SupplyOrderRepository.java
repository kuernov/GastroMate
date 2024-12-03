package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.supplyOrder.SupplyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyOrderRepository extends JpaRepository<SupplyOrder, Integer> {
    List<SupplyOrder> findTop10ByUserIdOrderByOrderDateAsc(Integer userId);

}
