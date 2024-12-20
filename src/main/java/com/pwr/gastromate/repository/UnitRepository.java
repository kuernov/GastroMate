package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Optional<Unit> findByIdAndUserId(Integer id, Integer userId);
    List<Unit> findByUserId(Integer userId);

}
