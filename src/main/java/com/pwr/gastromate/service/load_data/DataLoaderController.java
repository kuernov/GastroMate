package com.pwr.gastromate.service.load_data;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.service.MenuItemService;
import com.pwr.gastromate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/load")
public class DataLoaderController {

    @Autowired
    private LoadIngredientService ingredientService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoadMenuItemService menuItemService;

    @Autowired
    private LoadOrdersService ordersService;

    @Autowired
    private LoadRequiredQuantitiesService quantitiesService;

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
    public ResponseEntity<String> loadIngredients(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ingredientService.loadIngredientsFromExcel(filePath, user);
        return ResponseEntity.ok("Ingredients loaded successfully from skibidi " + filePath);
    }

    @PostMapping("/load-required-quantities")
    public ResponseEntity<String> loadQuantities(){
        try {
            quantitiesService.setQuantities();
            return ResponseEntity.ok("Quantities set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to load quantities: " + e.getMessage());
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
            menuItemService.deleteMenuItemsByUserId(1);
            return ResponseEntity.ok("Menu items for user ID " + 4 + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete menu items for user ID " + 1 + ": " + e.getMessage());
        }
    }
}
