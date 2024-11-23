package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.dto.CategoryRevenueDTO;
import com.pwr.gastromate.dto.SalesByDayOfWeekDTO;
import com.pwr.gastromate.dto.SalesByHourDTO;
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
import java.util.stream.Collectors;

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
        return orderItemRepository.findTotalRevenueBetweenDates(convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
    }

    public List<CategoryRevenueDTO> getRevenueByCategory(LocalDate startDate, LocalDate endDate, Integer userId) {
        return orderItemRepository.getRevenueByCategory(
                convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
    }

    public List<TopSellingItemDTO> getTopSellingItems(LocalDate startDate, LocalDate endDate, Integer userId) {
        List<TopSellingItemDTO> items =  orderItemRepository.findTopSellingItems(
                convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
        BigDecimal totalRevenue = orderItemRepository.findTotalRevenueBetweenDates(convertToStartOfDay(
                startDate), convertToEndOfDay(endDate), userId);
        long totalCount = orderRepository.countByOrderDateForUser(
                convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
        return items.stream()
                .map(data -> {
                    data.setRevenuePercentage((data.getTotalRevenue().doubleValue() / totalRevenue.doubleValue()) * 100);
                    data.setQuantityPercentage(((double) data.getQuantitySold()/totalCount)*100);
                    return data;
                })
                .collect(Collectors.toList());
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
    public List<SalesByDayOfWeekDTO> getSalesByDayOfWeek(LocalDate startDate, LocalDate endDate, Integer userId) {
        List<Object[]> results = orderItemRepository.findSalesByDayOfWeek(convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
        return results.stream()
                .map(result -> new SalesByDayOfWeekDTO(
                        ((Number) result[0]).intValue(), // numer dnia tygodnia
                        ((Number) result[1]).longValue(), // totalQuantity
                        (BigDecimal) result[2]           // totalRevenue
                ))
                .collect(Collectors.toList());
    }

    public List<SalesByHourDTO> getSalesByHour(LocalDate startDate, LocalDate endDate, Integer userId) {
        List<Object[]> results = orderItemRepository.findSalesByHour(convertToStartOfDay(startDate), convertToEndOfDay(endDate), userId);
        return results.stream()
                .map(result -> new SalesByHourDTO(
                        String.format("%02d:00", ((Number) result[0]).intValue()), // godzina
                        ((Number) result[1]).longValue(),                         // totalQuantity
                        (BigDecimal) result[2]                                    // totalRevenue
                ))
                .collect(Collectors.toList());
    }
}
