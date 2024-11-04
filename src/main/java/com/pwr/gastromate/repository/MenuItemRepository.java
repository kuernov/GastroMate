package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByUserId(Integer userId);

    boolean existsByName(String pizzaName);

    MenuItem findByName(String pizzaName);

    @Query("SELECT DISTINCT mi FROM MenuItem mi JOIN mi.menuItemIngredients mii JOIN mii.ingredient i WHERE i.id IN :ingredientIds AND mi.user.id = :userId")
    List<MenuItem> findByMenuItemIngredients(@Param("ingredientIds") List<Integer> ingredientIds, @Param("userId") Integer userId);

}
