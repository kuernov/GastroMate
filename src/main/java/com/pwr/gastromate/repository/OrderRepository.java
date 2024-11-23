package com.pwr.gastromate.repository;


import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.DailyIngredientUsageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order> {
    Page<Order> findByUser(User user, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.user.id = :userId")
    List<Order> findAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") Integer userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :startOfDay AND :endOfDay AND o.user.id = :userId")
    long countByOrderDateForUser(@Param("startOfDay") LocalDateTime startOfDay,
                                 @Param("endOfDay") LocalDateTime endOfDay,
                                 @Param("userId") Integer userId);

    @Query("""
    SELECT CAST(DATE(o.orderDate) AS java.sql.Date) AS date,
           COALESCE(CAST(SUM(oi.quantity * mii.quantityRequired) AS java.math.BigDecimal), 0) AS totalUsage
    FROM Order o
    JOIN o.orderItems oi
    JOIN oi.menuItem mi
    JOIN mi.menuItemIngredients mii
    JOIN mii.ingredient i
    WHERE i.id = :ingredientId
    GROUP BY DATE(o.orderDate)
    ORDER BY DATE(o.orderDate)
""")
    List<Object[]> findDailyIngredientUsage(@Param("ingredientId") Integer ingredientId);



    @Query(value = "SELECT EXTRACT(DOW FROM daily_usage.order_day) AS day_of_week, " +
            "AVG(daily_usage.total_quantity) AS average_usage " +
            "FROM (" +
            "    SELECT DATE(o.order_date) AS order_day, " +
            "           SUM(oi.quantity * mii.quantity_required) AS total_quantity " +
            "    FROM orders o " +
            "    JOIN order_items oi ON o.id = oi.order_id " +
            "    JOIN menu_items mi ON oi.menu_item_id = mi.id " +
            "    JOIN menu_item_ingredients mii ON mi.id = mii.menu_item_id " +
            "    JOIN ingredients i ON mii.ingredient_id = i.id " +
            "    WHERE i.id = :ingredientId " +
            "    GROUP BY DATE(o.order_date) " +
            ") AS daily_usage " +
            "GROUP BY day_of_week " +
            "ORDER BY day_of_week",
            nativeQuery = true)
    List<Object[]> findAverageUsageByDayOfWeekNative(@Param("ingredientId") Integer ingredientId);



}
