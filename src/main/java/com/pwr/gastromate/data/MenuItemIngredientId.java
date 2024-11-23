package com.pwr.gastromate.data;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MenuItemIngredientId implements Serializable {

    private Integer menuItemId;
    private Integer ingredientId;

}
