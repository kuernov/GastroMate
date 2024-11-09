package com.pwr.gastromate.service;

import com.pwr.gastromate.data.InventoryLog;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.InventoryLogDTO;
import com.pwr.gastromate.repository.InventoryLogRepository;
import com.pwr.gastromate.service.mapper.InventoryLogMapper;
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
    public List<InventoryLogDTO> save(User user, List<InventoryLogDTO> inventoryLogDTOS) {
        List<InventoryLog> inventoryLogIngredients = inventoryLogMapper.toEntityList(inventoryLogDTOS, user);
        List<InventoryLog> savedLogs = inventoryLogRepository.saveAll(inventoryLogIngredients);
        return inventoryLogMapper.toDtoList(savedLogs);
    }

    public List<InventoryLogDTO> findAll(User user){
        List<InventoryLog> logs = inventoryLogRepository.findByUserId(user.getId());
        return inventoryLogMapper.toDtoList(logs);
    }
}
