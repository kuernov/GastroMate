package com.pwr.gastromate.controller;

import com.pwr.gastromate.data.Unit;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.UnitDTO;
import com.pwr.gastromate.service.UnitService;
import com.pwr.gastromate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/units")
public class UnitController {
    private final UnitService unitService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Unit> getUnitById(@PathVariable Integer id) {
        Optional<Unit> unit = unitService.getUnitById(id);
        return unit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Unit> addUnit(@RequestBody UnitDTO unitDTO, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Unit createdUnit = unitService.addUnit(unitDTO, user);
        return ResponseEntity.ok(createdUnit);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Integer id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }
}
