package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.*;
import com.pwr.gastromate.dto.InventoryLogDTO;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.UnitRepository;
import com.pwr.gastromate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class InventoryLogMapper {
    private final UnitRepository unitRepository;
    private final IngredientRepository ingredientRepository;
    public InventoryLog toEntity(InventoryLogDTO inventoryLogDTO, User user){
        if(inventoryLogDTO==null)
            return null;
        Unit unit = unitRepository.findById(inventoryLogDTO.getUnitId())
                .orElseThrow(()-> new ResourceNotFoundException("Unit not found"));
        Ingredient ingredient = ingredientRepository.findById(inventoryLogDTO.getIngredientId())
                .orElseThrow(()-> new ResourceNotFoundException("Unit not found"));
        ChangeType type;
        try {
            type = ChangeType.valueOf(inventoryLogDTO.getChangeType().toUpperCase()); // Convert String to ChangeType enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid change type." );
        }
        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setUnit(unit);
        inventoryLog.setIngredient(ingredient);
        inventoryLog.setChangeType(type);
        inventoryLog.setUser(user);
        inventoryLog.setQuantityChange(inventoryLogDTO.getQuantityChange());
        return inventoryLog;
    }

    public InventoryLogDTO toDTO(InventoryLog inventoryLog){
        if(inventoryLog==null)
            return null;
        InventoryLogDTO inventoryLogDTO = new InventoryLogDTO();
        inventoryLogDTO.setIngredientId(inventoryLog.getIngredient().getId());
        inventoryLogDTO.setUnitId(inventoryLog.getUnit().getId());
        inventoryLogDTO.setChangeType(inventoryLog.getChangeType().toString());
        inventoryLogDTO.setQuantityChange(inventoryLog.getQuantityChange());
        inventoryLogDTO.setIngredientName(inventoryLogDTO.getIngredientName());
        inventoryLogDTO.setTimestamp(inventoryLog.getTimestamp());
        inventoryLogDTO.setUnitName(inventoryLogDTO.getUnitName());
        return inventoryLogDTO;
    }
    public List<InventoryLog> toEntityList(List<InventoryLogDTO> dtoList, User user) {
        return dtoList.stream()
                .map(dto -> toEntity(dto, user))
                .toList();
    }

    public List<InventoryLogDTO> toDtoList(List<InventoryLog> entityList) {
        return entityList.stream()
                .map(this::toDTO)
                .toList();
    }
}

