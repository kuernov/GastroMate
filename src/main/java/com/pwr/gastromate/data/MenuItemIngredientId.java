package com.pwr.gastromate.data;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
public class MenuItemIngredientId implements Serializable {

    private Integer menuItemId;
    private Integer ingredientId;

}
