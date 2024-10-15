package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.OrderItemDTO;
import com.pwr.gastromate.service.OrderGenerationService;
import com.pwr.gastromate.service.OrderService;
import com.pwr.gastromate.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
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
    private final OrderGenerationService orderGenerationService;

    @GetMapping
    @RolesAllowed("USER")
    public ResponseEntity<List<Order>> getUserOrders(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        User user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new ResponseEntity<>(user.getOrders(),HttpStatus.OK);
    }

    @PostMapping("/create")
    @RolesAllowed("USER")
    public ResponseEntity<Order> createOrder(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        orderGenerationService.generateOrders(10,user);
        // Return the created order and a 201 CREATED status
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
