package com.pwr.gastromate.repository;

import com.pwr.gastromate.data.MenuItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer>, JpaSpecificationExecutor<MenuItem> {
    List<MenuItem> findByUserId(Integer userId, Sort sort);

    default List<MenuItem> findByUserIdSorted(Integer userId) {
        return findByUserId(userId, Sort.by(Sort.Direction.ASC, "name"));
    }

    boolean existsByName(String pizzaName);

    MenuItem findByName(String pizzaName);


}
