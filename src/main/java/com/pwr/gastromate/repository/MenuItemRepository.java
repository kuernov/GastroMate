package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.menuItem.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer>, JpaSpecificationExecutor<MenuItem> {


    boolean existsByName(String pizzaName);

    MenuItem findByName(String pizzaName);


}
