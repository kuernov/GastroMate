package com.pwr.gastromate.specification;

import com.pwr.gastromate.data.Ingredient;
import org.springframework.data.jpa.domain.Specification;

public class IngredientSpecification {
    public static Specification<Ingredient> belongsToUser(Integer userId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Ingredient> hasNameContaining(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // No filtering if name is null or empty
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Ingredient> orderByNameAndExpiryDate() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(
                    criteriaBuilder.asc(root.get("name")),
                    criteriaBuilder.asc(root.get("expiryDate"))
            );
            return null; // Return null as the Specification does not need a predicate for ordering
        };
    }

}
