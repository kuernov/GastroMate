package com.pwr.gastromate.specification;

import com.pwr.gastromate.data.menuItem.MenuItem;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
public class MenuItemSpecification {
     public static Specification<MenuItem> hasSizeInName(String size){
          return (root, query, criteriaBuilder) ->
                  criteriaBuilder.like(root.get("name"), "%" + size + "%");
     }

     public static Specification<MenuItem> hasCategoryName(String categoryName) {
          return (root, query, criteriaBuilder) ->
                  criteriaBuilder.equal(root.join("categories").get("name"), categoryName);
     }

     public static Specification<MenuItem> hasIngredients(List<String> ingredients) {
          return (root, query, criteriaBuilder) ->
                  root.join("menuItemIngredients")
                          .join("ingredient")
                          .get("name")
                          .in(ingredients);
     }

     public static Specification<MenuItem> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
          return (root, query, criteriaBuilder) -> {
               if (minPrice != null && maxPrice != null) {
                    return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
               } else if (minPrice != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
               } else if (maxPrice != null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
               }
               return criteriaBuilder.conjunction();
          };
     }

     public static Specification<MenuItem> hasName(String name) {
          return (root, query, criteriaBuilder) ->
                  criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
     }
}
