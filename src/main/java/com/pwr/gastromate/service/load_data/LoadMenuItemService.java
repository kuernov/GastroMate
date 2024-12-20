package com.pwr.gastromate.service.load_data;

import com.pwr.gastromate.data.*;
import com.pwr.gastromate.data.menuItem.MenuItem;
import com.pwr.gastromate.data.menuItem.MenuItemIngredient;
import com.pwr.gastromate.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Service
public class LoadMenuItemService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoadMenuItemService.class);



    @Transactional
    public void loadMenuItemsFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Unit unit = unitRepository.findById(1).orElseThrow();
            Sheet sheet = workbook.getSheet("pizza_sales");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                // Pobieranie danych dotyczących elementu menu
                String pizzaName = row.getCell(11).getStringCellValue();
                String pizzaSize = row.getCell(8).getStringCellValue();
                String fullName = pizzaName + " (" + pizzaSize + ")";
                String pizzaDescription = "Delicious " + pizzaName;
                BigDecimal price = BigDecimal.valueOf(row.getCell(6).getNumericCellValue()); // Kolumna H - unit_price

                // Pobieranie kategorii i składników
                String categoryName = row.getCell(9).getStringCellValue();
                String[] ingredientsArray = row.getCell(10).getStringCellValue().split(","); // Kolumna K - pizza_ingredients

                // Sprawdzanie, czy istnieje już kategoria
                Category category = categoryRepository.findByName(categoryName);
                if (!menuItemRepository.existsByName(fullName)) {
                    // Tworzenie nowego MenuItem
                    MenuItem menuItem = new MenuItem();
                    menuItem.setName(fullName);
                    menuItem.setDescription(pizzaDescription);
                    menuItem.setPrice(price);
                    menuItem.setUser(userRepository.findById(1).orElseThrow()); // Przykład ID użytkownika

                    // Dodanie kategorii
                    menuItem.getCategories().add(category);

                    // Dodawanie składników do MenuItem
                    Set<MenuItemIngredient> menuItemIngredients = new HashSet<>();
                    for (String ingredientName : ingredientsArray) {
                        ingredientName = ingredientName.trim();
                        Ingredient ingredient = ingredientRepository.findByName(ingredientName);

                        // Tworzenie powiązania MenuItemIngredient
                        MenuItemIngredient menuItemIngredient = new MenuItemIngredient();
                        menuItemIngredient.setMenuItem(menuItem);
                        menuItemIngredient.setIngredient(ingredient);
                        menuItemIngredient.setQuantityRequired(new BigDecimal("1")); // Przykładowa ilość, można dostosować
                        menuItemIngredient.setUnit(unit);
                        menuItemIngredients.add(menuItemIngredient);
                    }
                    menuItem.setMenuItemIngredients(menuItemIngredients);

                    // Zapisywanie MenuItem do bazy danych
                    menuItemRepository.save(menuItem);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

