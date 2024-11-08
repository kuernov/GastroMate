package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.data.OrderItemId;
import com.pwr.gastromate.dto.CategoryRevenueDTO;
import com.pwr.gastromate.dto.SalesByDayOfWeekDTO;
import com.pwr.gastromate.dto.SalesByHourDTO;
import com.pwr.gastromate.dto.TopSellingItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
    @Query("SELECT new com.pwr.gastromate.dto.TopSellingItemDTO(oi.menuItem.name, SUM(oi.quantity), SUM(oi.priceAtOrder * oi.quantity)) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderDate BETWEEN :startDate AND :endDate " +
            "AND oi.order.user.id = :userId " +
            "GROUP BY oi.menuItem.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<TopSellingItemDTO> findTopSellingItems(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                @Param("userId") Integer userId);

    @Query("SELECT COALESCE(SUM(oi.priceAtOrder * oi.quantity), 0) " +
            "FROM OrderItem oi JOIN oi.order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.user.id = :userId")
    BigDecimal findTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("userId") Integer userId);

    @Query("SELECT new com.pwr.gastromate.dto.CategoryRevenueDTO(c.name, SUM(oi.priceAtOrder * oi.quantity), SUM(oi.quantity)) " +
            "FROM OrderItem oi JOIN oi.menuItem m JOIN m.categories c " +
            "WHERE oi.order.orderDate BETWEEN :startDate AND :endDate " +
            "AND oi.order.user.id = :userId " +
            "GROUP BY c.name")
    List<CategoryRevenueDTO> getRevenueByCategory(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") Integer userId);

    @Query(value = "SELECT (CASE WHEN EXTRACT(DOW FROM o.order_date) = 0 THEN 7 ELSE EXTRACT(DOW FROM o.order_date) END) AS dayOfWeek, " +
            "SUM(oi.quantity) AS totalQuantity, SUM(oi.price_at_order * oi.quantity) AS totalRevenue " +
            "FROM order_items oi JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.order_date BETWEEN :startDate AND :endDate AND o.user_id = :userId " +
            "GROUP BY dayOfWeek " +
            "ORDER BY dayOfWeek", nativeQuery = true)
    List<Object[]> findSalesByDayOfWeek(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Integer userId);

    @Query(value = "SELECT EXTRACT(HOUR FROM o.order_date) AS hour, SUM(oi.quantity) AS totalQuantity, SUM(oi.price_at_order * oi.quantity) AS totalRevenue " +
            "FROM order_items oi JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.order_date BETWEEN :startDate AND :endDate AND o.user_id = :userId " +
            "GROUP BY EXTRACT(HOUR FROM o.order_date) " +
            "ORDER BY hour", nativeQuery = true)
    List<Object[]> findSalesByHour(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         @Param("userId") Integer userId);

}
