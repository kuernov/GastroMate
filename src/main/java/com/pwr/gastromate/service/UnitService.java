package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Unit;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.UnitDTO;
import com.pwr.gastromate.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnitService {
    private final UnitRepository unitRepository;

    @Autowired
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Optional<Unit> getUnitById(Integer id) {
        return unitRepository.findById(id);
    }

    public Unit addUnit(UnitDTO unitDTO, User user) {
        Unit unit = new Unit();
        unit.setUser(user);
        unit.setName(unitDTO.getName());
        unit.setAbbreviation(unitDTO.getAbbreviation());
        unit.setConversionToGrams(unitDTO.getConversionToGrams());
        return unitRepository.save(unit);
    }

    public void deleteUnit(Integer id) {
        unitRepository.deleteById(id);
    }

}
