package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.dto.CategoryRevenueDTO;
import com.pwr.gastromate.dto.TopSellingItemDTO;
import com.pwr.gastromate.repository.OrderItemRepository;
import com.pwr.gastromate.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SalesReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private LocalDateTime convertToStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime convertToEndOfDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }

    public BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate, Integer userId) {
        LocalDateTime startDateTime = convertToStartOfDay(startDate);
        LocalDateTime endDateTime = convertToEndOfDay(endDate);
        return orderItemRepository.findTotalRevenueBetweenDates(startDateTime, endDateTime, userId);
    }

    public List<CategoryRevenueDTO> getRevenueByCategory(LocalDate startDate, LocalDate endDate, Integer userId) {
        return orderItemRepository.getRevenueByCategory(
                convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
    }

    public List<TopSellingItemDTO> getTopSellingItems(LocalDate startDate, LocalDate endDate, Integer userId) {
        return orderItemRepository.findTopSellingItems(
                convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
    }

    public long calculateOrdersCountByDate(LocalDate startDate, LocalDate endDate, Integer userId) {
        return orderRepository.countByOrderDateForUser(
                convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
    }

    public BigDecimal calculateAverageOrderValue(LocalDate startDate, LocalDate endDate, Integer userId) {
        BigDecimal totalRevenue = calculateTotalRevenue(startDate, endDate, userId);
        long totalOrders = calculateOrdersCountByDate(startDate, endDate, userId);
        return totalOrders > 0 ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
}
