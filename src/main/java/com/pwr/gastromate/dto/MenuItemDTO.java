package com.pwr.gastromate.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MenuItemDTO {
    private String name;
    private String description;
    private Double price;
    private Set<Integer> categoryIds;
}
