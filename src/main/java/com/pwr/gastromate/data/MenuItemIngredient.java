package com.pwr.gastromate.data;

import jakarta.persistence.*;

@Entity
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
    private Ingredient ingredient;

    @Column(nullable = false)
    private Float quantityRequired;

    // Constructors, getters, setters
}
