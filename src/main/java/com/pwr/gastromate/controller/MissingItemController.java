package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.MissingItemsResponse;
import com.pwr.gastromate.exception.UnauthorizedException;
import com.pwr.gastromate.service.MissingItemsService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/restock")
public class MissingItemController {
    private final UserService userService;
    private final MissingItemsService missingItemsService;

    private User findUser(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthorized: Principal is null");
        }
        String username = principal.getName();
        return userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @GetMapping("/low-stock-items")
    public ResponseEntity<MissingItemsResponse> fetchMissingItems (Principal principal){
        User user = findUser(principal);
        MissingItemsResponse response = missingItemsService.findLowStockIngredients(user);
        return ResponseEntity.ok(response);
    }

}
