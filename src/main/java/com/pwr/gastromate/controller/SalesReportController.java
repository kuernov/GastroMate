package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.report.CategoryRevenueDTO;
import com.pwr.gastromate.dto.report.SalesByDayOfWeekDTO;
import com.pwr.gastromate.dto.report.SalesByHourDTO;
import com.pwr.gastromate.dto.report.TopSellingItemDTO;
import com.pwr.gastromate.exception.UnauthorizedException;
import com.pwr.gastromate.service.SalesReportService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class SalesReportController {
    private final SalesReportService salesReportService;
    private final UserService userService;

    private User findUser(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthorized: Principal is null");
        }
        String username = principal.getName();
        return userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        BigDecimal totalRevenue = salesReportService.calculateTotalRevenue(startDate, endDate, userId);
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/category-revenue")
    public ResponseEntity<List<CategoryRevenueDTO>> getRevenueByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        List<CategoryRevenueDTO> categoryRevenue = salesReportService.getRevenueByCategory(startDate, endDate, userId);
        return ResponseEntity.ok(categoryRevenue);
    }

    @GetMapping("/top-selling-items")
    public ResponseEntity<List<TopSellingItemDTO>> getTopSellingItems(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        List<TopSellingItemDTO> topSellingItems = salesReportService.getTopSellingItems(startDate, endDate, userId);
        return ResponseEntity.ok(topSellingItems);
    }

    @GetMapping("/orders-count")
    public ResponseEntity<Long> getOrdersCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        long ordersCount = salesReportService.calculateOrdersCountByDate(startDate, endDate, userId);
        return ResponseEntity.ok(ordersCount);
    }

    @GetMapping("/average-order-value")
    public ResponseEntity<BigDecimal> getAverageOrderValue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        BigDecimal averageOrderValue = salesReportService.calculateAverageOrderValue(startDate, endDate, userId);
        return ResponseEntity.ok(averageOrderValue);
    }

    @GetMapping("/sales-by-day-of-week")
    public ResponseEntity<List<SalesByDayOfWeekDTO>> getSalesByDayOfWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        List<SalesByDayOfWeekDTO> sales = salesReportService.getSalesByDayOfWeek(startDate, endDate, userId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/sales-by-hour")
    public ResponseEntity<List<SalesByHourDTO>> getSalesByHour(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {
        User user = findUser(principal);
        Integer userId = user.getId();
        List<SalesByHourDTO> sales = salesReportService.getSalesByHour(startDate, endDate, userId);
        return ResponseEntity.ok(sales);
    }
}
