package com.pwr.gastromate.repository;


import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order> {
    Page<Order> findByUser(User user, Pageable pageable);


}
