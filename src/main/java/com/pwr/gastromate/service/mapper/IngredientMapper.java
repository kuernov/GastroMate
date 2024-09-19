package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.Unit;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.IngredientDTO;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    // Konwersja z encji Ingredient na DTO
    public IngredientDTO toDTO(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getQuantity(),
                ingredient.getUnit().getName(),  // Pobieramy nazwę jednostki (zakładam, że Unit ma metodę getName())
                ingredient.getExpiryDate(),
                ingredient.getUser().getId()  // Pobieramy ID użytkownika
        );
    }

    // Konwersja z DTO do encji Ingredient
    public Ingredient toEntity(IngredientDTO ingredientDTO, Unit unit, User user) {
        if (ingredientDTO == null) {
            return null;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientDTO.getId());
        ingredient.setName(ingredientDTO.getName());
        ingredient.setQuantity(ingredientDTO.getQuantity());
        ingredient.setUnit(unit);  // Ustawiamy jednostkę na podstawie przekazanego obiektu Unit
        ingredient.setExpiryDate(ingredientDTO.getExpiryDate());
        ingredient.setUser(user);  // Ustawiamy użytkownika na podstawie przekazanego obiektu User

        return ingredient;
    }
}
