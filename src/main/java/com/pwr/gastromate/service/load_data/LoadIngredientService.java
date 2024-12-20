package com.pwr.gastromate.service.load_data;

import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.Unit;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.ingredient.IngredientDTO;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.UnitRepository;
import com.pwr.gastromate.repository.UserRepository;
import com.pwr.gastromate.service.mapper.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
@Service
public class LoadIngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private UnitRepository unitRepository;
    @Transactional
    public void loadIngredientsFromExcel(String filePath, User user) {
        Set<String> uniqueIngredients = new HashSet<>();
        int userId=user.getId();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("pizza_sales");
            for (Row row : sheet) {
                Cell ingredientsCell = row.getCell(10);
                if (ingredientsCell != null && ingredientsCell.getCellType() == CellType.STRING) {
                    String[] ingredients = ingredientsCell.getStringCellValue().split(",");
                    for (String ingredient : ingredients) {
                        uniqueIngredients.add(ingredient.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Unit unit = unitRepository.findByIdAndUserId(1,user.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Unit not found"));
        // Dodanie unikalnych składników do bazy danych
        for (String ingredientName : uniqueIngredients) {
            if (!ingredientRepository.existsByName(ingredientName)) {
                IngredientDTO ingredientDTO = new IngredientDTO();
                ingredientDTO.setName(ingredientName);
                ingredientDTO.setQuantity(0F);
                ingredientDTO.setUnitId(user.getId());  // Przykładowe unityId
                ingredientDTO.setExpiryDate(null);
                Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO, unit);
                ingredient.setUser(user);
                ingredientRepository.save(ingredient);
            }
        }
    }
}