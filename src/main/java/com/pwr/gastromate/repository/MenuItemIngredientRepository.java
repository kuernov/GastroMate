package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.MenuItemIngredient;
import com.pwr.gastromate.data.MenuItemIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, MenuItemIngredientId> {
}
