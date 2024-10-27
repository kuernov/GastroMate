package com.pwr.gastromate.service.load_data;

import com.pwr.gastromate.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataLoaderController {

    @Autowired
    private LoadIngredientService ingredientService;

    @Autowired
    private LoadMenuItemService menuItemService;

    @Autowired
    private LoadOrdersService ordersService;
    private final String filePath = "src/main/resources/Data Model - Pizza Sales.xlsx";

    @PostMapping("/load-menu-items")
    public ResponseEntity<String> loadMenuItems() {
        try {
            menuItemService.loadMenuItemsFromExcel(filePath);
            return ResponseEntity.ok("Menu items loaded successfully from " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to load menu items: " + e.getMessage());
        }
    }
    @PostMapping("/load-ingredients")
    public ResponseEntity<String> loadIngredients(){
        try {
            ingredientService.loadIngredientsFromExcel(filePath);
            return ResponseEntity.ok("Ingredients loaded successfully from " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to load ingredients: " + e.getMessage());
        }
    }

    @PostMapping("/load-orders")
    public ResponseEntity<String> loadOrders(){
        try {
            ordersService.loadOrdersFromExcel(filePath);
            return ResponseEntity.ok("Orders loaded successfully from " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to load orders: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-menu-items-by-user")
    public ResponseEntity<String> deleteMenuItemsByUser() {
        try {
            menuItemService.deleteMenuItemsByUserId(4);
            return ResponseEntity.ok("Menu items for user ID " + 4 + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete menu items for user ID " + 4 + ": " + e.getMessage());
        }
    }
}
