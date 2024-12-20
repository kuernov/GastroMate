package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.data.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer>, JpaSpecificationExecutor<Ingredient> {
    @Query("SELECT i FROM Ingredient i WHERE i.user.id = :userId ORDER BY i.name ASC, i.expiryDate ASC")
    List<Ingredient> findByUserId(Integer userId);
    boolean existsByName(String name);
    Ingredient findByName(String name);
    @Query("SELECT COALESCE(SUM(q.quantity), 0) " +
            "FROM Ingredient q " +
            "WHERE q.name = :name AND q.user.id = :id")
    BigDecimal sumQuantityByNameAndUserId(@Param("name") String name, @Param("id") Integer id);
}
