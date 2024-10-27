package com.pwr.gastromate.service.load_data;

import com.pwr.gastromate.data.*;
import com.pwr.gastromate.exception.ResourceNotFoundException;
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
import java.util.List;
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
    public void deleteMenuItemsByUserId(Integer userId) {
        // Znajdź wszystkie elementy menu powiązane z użytkownikiem
        List<MenuItem> userMenuItems = menuItemRepository.findByUserId(userId);

        // Usuń wszystkie znalezione elementy
        menuItemRepository.deleteAll(userMenuItems);
    }

    @Transactional
    public void loadMenuItemsFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Unit unit = unitRepository.findById(16).orElseThrow();
            Sheet sheet = workbook.getSheet("pizza_sales");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                // Pobieranie danych dotyczących elementu menu
                String pizzaName = row.getCell(11).getStringCellValue(); // Kolumna L - pizza_name
                String pizzaSize = row.getCell(8).getStringCellValue(); // Kolumna I - pizza_size
                String fullName = pizzaName + " (" + pizzaSize + ")"; // Łączenie nazwy z rozmiarem
                String pizzaDescription = "Delicious " + pizzaName; // Przykładowy opis, możesz dostosować
                BigDecimal price = BigDecimal.valueOf(row.getCell(7).getNumericCellValue()); // Kolumna H - unit_price

                // Pobieranie kategorii i składników
                String categoryName = row.getCell(9).getStringCellValue(); // Kolumna J - pizza_category
                String[] ingredientsArray = row.getCell(10).getStringCellValue().split(","); // Kolumna K - pizza_ingredients

                // Sprawdzanie, czy istnieje już kategoria
                Category category = categoryRepository.findByName(categoryName);
                if (!menuItemRepository.existsByName(fullName)) {
                    // Tworzenie nowego MenuItem
                    MenuItem menuItem = new MenuItem();
                    menuItem.setName(fullName);
                    menuItem.setDescription(pizzaDescription);
                    menuItem.setPrice(price);
                    menuItem.setUser(userRepository.findById(4).orElseThrow()); // Przykład ID użytkownika

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
                        menuItemIngredient.setQuantityRequired(1.0f); // Przykładowa ilość, można dostosować
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

