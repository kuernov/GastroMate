package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.menuItem.MenuItemIngredient;
import com.pwr.gastromate.data.menuItem.MenuItemIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, MenuItemIngredientId> , JpaSpecificationExecutor<MenuItemIngredient> {
}
