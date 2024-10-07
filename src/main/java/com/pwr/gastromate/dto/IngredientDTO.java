package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

    private Integer id;
    private String name;
    private Float quantity;
    private Integer unitId;
    private LocalDate expiryDate;

}
