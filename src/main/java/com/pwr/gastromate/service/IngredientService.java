package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.Unit;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.IngredientDTO;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.UnitRepository;
import com.pwr.gastromate.repository.UserRepository;
import com.pwr.gastromate.service.mapper.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private IngredientMapper ingredientMapper;

    public List<IngredientDTO> getIngredientsByUserId(Integer userId) {
        List<Ingredient> ingredients = ingredientRepository.findByUserId(userId);
        return ingredients.stream()
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public IngredientDTO addIngredient(IngredientDTO ingredientDTO, User user) {
        Unit unit = unitRepository.findByIdAndUserId(ingredientDTO.getUnitId(), user.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Unit not found"));

        Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO, unit);
        ingredient.setUser(user);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return ingredientMapper.toDTO(savedIngredient);
    }

    // Pomocnicza metoda do tworzenia nowej jednostki dla użytkownika
    private Unit createNewUnit(String unitName, User user) {
        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setUser(user);
        return unitRepository.save(unit);  // Zapisanie nowej jednostki w bazie danych
    }

    public void deleteIngredient(Integer id){
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found for this id: " + id));
        ingredientRepository.delete(ingredient);
    }

    // Nowa metoda do aktualizacji ilości składnika
    public IngredientDTO updateIngredientQuantity(Integer ingredientId, Float newQuantity) {
        // Znajdź składnik po jego ID
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found for this id: " + ingredientId));
        ingredient.setQuantity(newQuantity);
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDTO(updatedIngredient);
    }

}
