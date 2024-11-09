package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {
    List<InventoryLog> findByUserId(Integer id);
}
