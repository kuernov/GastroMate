package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.data.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByUserId(Integer userId);
    boolean existsByName(String name);
    Ingredient findByName(String name);

}
