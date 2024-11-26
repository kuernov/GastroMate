package com.pwr.gastromate.service;

import com.pwr.gastromate.data.InventoryLog;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.InventoryLogDTO;
import com.pwr.gastromate.repository.InventoryLogRepository;
import com.pwr.gastromate.service.mapper.InventoryLogMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryLogService {
    private final InventoryLogRepository inventoryLogRepository;
    private final InventoryLogMapper inventoryLogMapper;
    @Transactional
    public InventoryLogDTO save(User user, InventoryLogDTO inventoryLogDTO) {
        InventoryLog inventoryLogIngredient = inventoryLogMapper.toEntity(inventoryLogDTO, user);
        System.out.println("Saving InventoryLog with changeType: " + inventoryLogIngredient.getChangeType());
        inventoryLogRepository.insertInventoryLog(
                inventoryLogIngredient.getIngredient().getId(),
                inventoryLogIngredient.getUnit().getId(),
                inventoryLogIngredient.getChangeType().toString(),
                inventoryLogIngredient.getQuantityChange(),
                user.getId()
        );
        return inventoryLogMapper.toDTO(inventoryLogIngredient);

    }

    public List<InventoryLogDTO> findAll(User user){
        List<InventoryLog> logs = inventoryLogRepository.findByUserId(user.getId());
        return inventoryLogMapper.toDtoList(logs);
    }
}
