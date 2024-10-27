package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByUserId(Integer userId);

    boolean existsByName(String pizzaName);

    MenuItem findByName(String pizzaName);
}
