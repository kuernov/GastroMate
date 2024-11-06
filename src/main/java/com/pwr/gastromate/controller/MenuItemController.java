package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.MenuItemDTO;
import com.pwr.gastromate.dto.MenuItemIngredientsRequest;
import com.pwr.gastromate.dto.MenuItemRequest;
import com.pwr.gastromate.service.MenuItemService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Integer id) {
        try {
            MenuItemDTO menuItemDTO = menuItemService.findById(id);
            return ResponseEntity.ok(menuItemDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<MenuItemDTO>> findAll(@RequestParam(required = false) String size,
                                                     @RequestParam(required = false) String category,
                                                     @RequestParam(required = false) List<String> ingredients,
                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<MenuItemDTO> menuItems = menuItemService.findAll(size, category, ingredients, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(menuItems);
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemRequest menuItemRequest, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        MenuItemDTO createdMenuItem = menuItemService.save(menuItemRequest.getMenuItem(), menuItemRequest.getMenuItemIngredients(), user);
        return ResponseEntity.ok(createdMenuItem);
    }

    // Zaktualizuj istniejący MenuItem na podstawie DTO
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Integer id, @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO updatedMenuItem = menuItemService.updateMenuItem(id, menuItemDTO);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Usuń MenuItem po ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Integer id) {
        menuItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

