package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.InventoryLog;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.InventoryLogDTO;
import com.pwr.gastromate.exception.UnauthorizedException;
import com.pwr.gastromate.service.InventoryLogService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inventory-log")
public class InventoryLogController {
    private final UserService userService;
    private final InventoryLogService inventoryLogService;

    private User findUser(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthorized: Principal is null");
        }
        String username = principal.getName();
        return userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @GetMapping
    public ResponseEntity<List<InventoryLogDTO>> getInventoryLogs(Principal principal){
        User user = findUser(principal);
        List<InventoryLogDTO> logs = inventoryLogService.findAll(user);
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryLogDTO> saveInventoryLog(Principal principal, @RequestBody InventoryLogDTO inventoryLogDTO){
        User user = findUser(principal);
        InventoryLogDTO log = inventoryLogService.save(user, inventoryLogDTO);
        return ResponseEntity.ok(log);
    }

}
