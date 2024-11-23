package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.ChangeType;
import com.pwr.gastromate.data.InventoryLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {
    List<InventoryLog> findByUserId(Integer id);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO inventory_log (ingredient_id, unit_id, change_type, quantity_change, timestamp, user_id) " +
            "VALUES (:ingredientId, :unitId, CAST(:changeType AS change_types), :quantityChange, CURRENT_TIMESTAMP, :userId)",
            nativeQuery = true)
    void insertInventoryLog(@Param("ingredientId") Integer ingredientId,
                            @Param("unitId") Integer unitId,
                            @Param("changeType") String changeType,
                            @Param("quantityChange") BigDecimal quantityChange,
                            @Param("userId") Integer userId);

}
