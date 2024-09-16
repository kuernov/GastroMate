package com.pwr.gastromate.controller;

import com.pwr.gastromate.dto.MenuItemDTO;
import com.pwr.gastromate.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Integer id) {
        try {
            MenuItemDTO menuItemDTO = menuItemService.findById(id);
            return ResponseEntity.ok(menuItemDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Utwórz nowy MenuItem na podstawie DTO
    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO createdMenuItem = menuItemService.save(menuItemDTO);
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

