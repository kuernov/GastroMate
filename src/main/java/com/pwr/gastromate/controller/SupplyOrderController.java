package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.SupplyOrderDTO;
import com.pwr.gastromate.dto.SupplyOrderItemRequest;
import com.pwr.gastromate.dto.SupplyOrderRequest;
import com.pwr.gastromate.exception.UnauthorizedException;
import com.pwr.gastromate.service.SupplyOrderService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/supply-order")
@AllArgsConstructor
public class SupplyOrderController {
    private final SupplyOrderService supplyOrderService;
    private final UserService userService;

    private User findUser(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthorized: Principal is null");
        }
        String username = principal.getName();
        return userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @PostMapping
    public ResponseEntity<String> createSupplyOrder(@RequestBody SupplyOrderRequest supplyOrderItemRequests, Principal principal) {
        User user = findUser(principal);
        supplyOrderService.createSupplyOrder(supplyOrderItemRequests,user);
        return ResponseEntity.ok("Supply order created successfully!");
    }

    @GetMapping("/last-orders")
    public ResponseEntity<List<SupplyOrderDTO>> getLastOrders(Principal principal) {
        User user = findUser(principal);
        List<SupplyOrderDTO> lastOrders = supplyOrderService.findLast10Orders(user);
        return ResponseEntity.ok(lastOrders);
    }
}
