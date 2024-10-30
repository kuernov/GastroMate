package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.GroupedIngredientDTO;
import com.pwr.gastromate.dto.IngredientDTO;
import com.pwr.gastromate.service.IngredientService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private final UserService userService;

    // Pobieranie wszystkich składników dla użytkownika
    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getIngredientsByUserId(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<IngredientDTO> ingredients = ingredientService.getIngredientsByUserId(user.getId());
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/grouped")
    public ResponseEntity<List<GroupedIngredientDTO>> getGroupedIngredients(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(ingredientService.getGroupedIngredients(user.getId()));
    }

    // Dodawanie składnika dla użytkownika
    @PostMapping
    public ResponseEntity<IngredientDTO> addIngredient(
            Principal principal,
            @RequestBody IngredientDTO ingredientDTO) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        IngredientDTO createdIngredient = ingredientService.addIngredient(ingredientDTO, user);
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