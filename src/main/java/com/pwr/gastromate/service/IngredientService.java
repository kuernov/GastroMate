package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.Unit;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.ingredient.GroupedIngredientDTO;
import com.pwr.gastromate.dto.ingredient.IngredientDTO;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.InventoryLogRepository;
import com.pwr.gastromate.repository.UnitRepository;
import com.pwr.gastromate.service.mapper.IngredientMapper;
import com.pwr.gastromate.specification.IngredientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private IngredientMapper ingredientMapper;

    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    public List<IngredientDTO> getIngredientsByUserId(Integer userId) {
        List<Ingredient> ingredients = ingredientRepository.findByUserId(userId);
        return ingredients.stream()
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<GroupedIngredientDTO> getGroupedIngredients(Integer userId, String name) {
        // Combine specifications for user ID, name filtering, and ordering
        Specification<Ingredient> spec = Specification.where(IngredientSpecification.belongsToUser(userId))
                .and(IngredientSpecification.hasNameContaining(name))
                .and(IngredientSpecification.orderByNameAndExpiryDate());

        List<Ingredient> ingredients = ingredientRepository.findAll(spec);

        Map<String, List<Ingredient>> groupedByProductName = ingredients.stream()
                .collect(Collectors.groupingBy(Ingredient::getName));

        return groupedByProductName.entrySet().stream()
                .map(entry -> {
                    String productName = entry.getKey();
                    List<Ingredient> ingredientList = entry.getValue();

                    double totalQuantity = ingredientList.stream()
                            .mapToDouble(Ingredient::getQuantity)
                            .sum();

                    return new GroupedIngredientDTO(
                            productName,
                            totalQuantity,
                            ingredientList.stream()
                                    .map(ingredient -> new IngredientDTO(
                                            ingredient.getId(),
                                            ingredient.getName(),
                                            ingredient.getQuantity(),
                                            ingredient.getUnit().getId(),
                                            ingredient.getExpiryDate()
                                    ))
                                    .collect(Collectors.toList())
                    );
                })
                .sorted(Comparator.comparing(GroupedIngredientDTO::getName)) // Sort by product name in the final DTO list
                .collect(Collectors.toList());
    }

    public IngredientDTO addIngredient(IngredientDTO ingredientDTO, User user) {
        Unit unit = unitRepository.findById(ingredientDTO.getUnitId())
                .orElseThrow(()-> new ResourceNotFoundException("Unit not found"));

        Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO, unit);
        ingredient.setUser(user);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        inventoryLogRepository.insertInventoryLog(savedIngredient.getId(),unit.getId(),
                "ADD", BigDecimal.valueOf(savedIngredient.getQuantity()), user.getId());
        return ingredientMapper.toDTO(savedIngredient);
    }

    // Pomocnicza metoda do tworzenia nowej jednostki dla użytkownika
    private Unit createNewUnit(String unitName, User user) {
        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setUser(user);
        return unitRepository.save(unit);
    }

    public void deleteIngredient(Integer id){
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found for this id: " + id));
        inventoryLogRepository.insertInventoryLog(ingredient.getId(),ingredient.getUnit().getId(),
                "SUBTRACT", BigDecimal.valueOf(ingredient.getQuantity()), ingredient.getUser().getId());

        ingredientRepository.delete(ingredient);
    }

    public IngredientDTO updateIngredientQuantity(Integer ingredientId, Float quantityChange, String changeType) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found for this id: " + ingredientId));
        Float newQuantity = getNewQuantity(quantityChange, changeType, ingredient);
        ingredient.setQuantity(newQuantity);
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        inventoryLogRepository.insertInventoryLog(updatedIngredient.getId(),updatedIngredient.getUnit().getId(),
                changeType.toUpperCase(), BigDecimal.valueOf(quantityChange), updatedIngredient.getUser().getId());
        return ingredientMapper.toDTO(updatedIngredient);
    }

    private static Float getNewQuantity(Float quantityChange, String changeType, Ingredient ingredient) {
        Float newQuantity = ingredient.getQuantity();
        switch (changeType.toLowerCase()) {
            case "add":
                newQuantity += quantityChange;
                break;
            case "subtract":
                newQuantity -= quantityChange;
                if (newQuantity < 0) {
                    newQuantity = 0f;
                }
                break;
            case "adjust":
                newQuantity = quantityChange;
                break;
            default:
                throw new IllegalArgumentException("Invalid change type");
        }

        return newQuantity;
    }

}
