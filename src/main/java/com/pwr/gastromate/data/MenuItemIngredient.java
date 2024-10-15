package com.pwr.gastromate.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu_item_ingredients")
public class MenuItemIngredient {

    @EmbeddedId
    private MenuItemIngredientId id = new MenuItemIngredientId();

    @ManyToOne
    @MapsId("menuItemId")  // Mapa klucz menuItemId z MenuItemIngredientId do encji MenuItem
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @ManyToOne
    @MapsId("ingredientId")  // Mapa klucz ingredientId z MenuItemIngredientId do encji Ingredient
    @JoinColumn(name = "ingredient_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  // Usuwanie kaskadowe
    private Ingredient ingredient;

    @Column(nullable = false)
    private Float quantityRequired;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)  // Usuwanie kaskadowe
    private Unit unit;

    // Constructors, getters, setters
}
