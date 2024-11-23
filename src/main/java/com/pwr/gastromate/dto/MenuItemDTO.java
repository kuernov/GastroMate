package com.pwr.gastromate.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MenuItemDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Set<Integer> categoryIds;
    private List<MenuItemIngredientDTO> ingredients;

}
