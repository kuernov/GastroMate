package com.pwr.gastromate.specification;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.User;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Order> belongsToUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<Order> hasOrderDateAfter(LocalDateTime startDateTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), startDateTime);
    }

    public static Specification<Order> hasOrderDateBefore(LocalDateTime endDateTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("orderDate"), endDateTime);
    }
}

