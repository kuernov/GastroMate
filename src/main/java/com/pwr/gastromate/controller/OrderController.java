package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.OrderDTO;
import com.pwr.gastromate.dto.OrderItemDTO;
import com.pwr.gastromate.service.OrderService;
import com.pwr.gastromate.service.UserService;
import com.pwr.gastromate.service.mapper.OrderMapper;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;



    @GetMapping
    @RolesAllowed("USER")
    public ResponseEntity<Page<OrderDTO>> getUserOrders(Principal principal, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        User user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Page<OrderDTO> userOrders = orderService.getAllOrdersForUser(user, page, size);

        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }

}
