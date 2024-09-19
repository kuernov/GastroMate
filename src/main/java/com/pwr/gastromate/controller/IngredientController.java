package com.pwr.gastromate.controller;

import com.pwr.gastromate.dto.IngredientDTO;
import com.pwr.gastromate.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    // Pobieranie wszystkich składników dla użytkownika
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IngredientDTO>> getIngredientsByUserId(@PathVariable Integer userId) {
        List<IngredientDTO> ingredients = ingredientService.getIngredientsByUserId(userId);
        return ResponseEntity.ok(ingredients);
    }

    // Dodawanie składnika dla użytkownika
    @PostMapping("/user/{userId}")
    public ResponseEntity<IngredientDTO> addIngredient(
            @PathVariable Integer userId,
            @RequestBody IngredientDTO ingredientDTO) {
        IngredientDTO createdIngredient = ingredientService.addIngredient(ingredientDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
    }

    // Usuwanie składnika
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Integer ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
        return ResponseEntity.noContent().build();
    }

    // Aktualizacja ilości składnika
    @PutMapping("/{ingredientId}/quantity")
    public ResponseEntity<IngredientDTO> updateIngredientQuantity(
            @PathVariable Integer ingredientId,
            @RequestParam Float newQuantity) {
        IngredientDTO updatedIngredient = ingredientService.updateIngredientQuantity(ingredientId, newQuantity);
        return ResponseEntity.ok(updatedIngredient);
    }

}