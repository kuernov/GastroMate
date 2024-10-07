package com.pwr.gastromate.service;

import com.pwr.gastromate.data.*;
import com.pwr.gastromate.dto.MenuItemDTO;
import com.pwr.gastromate.dto.MenuItemIngredientDTO;
import com.pwr.gastromate.repository.CategoryRepository;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.UserRepository;
import com.pwr.gastromate.service.mapper.MenuItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    public List<MenuItemDTO> findAllByUserId(Integer userId) {
        List<MenuItem> menuItems = menuItemRepository.findByUserId(userId);
        return menuItems.stream()
                .map(MenuItemMapper::toDTO)  // Mapuj każdą encję na DTO
                .collect(Collectors.toList());
    }

    // Znajdź MenuItem po ID
    public MenuItemDTO findById(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MenuItem not found"));
        return MenuItemMapper.toDTO(menuItem);
    }
    public MenuItemDTO save(MenuItemDTO menuItemDTO, List<MenuItemIngredientDTO> menuItemIngredientDTOS, User user) {
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(menuItemDTO.getCategoryIds()));
        MenuItem menuItem = MenuItemMapper.toEntity(menuItemDTO, categories);
        menuItem.setUser(user);
        List<MenuItemIngredient> menuItemIngredients = new ArrayList<>();
        for(MenuItemIngredientDTO menuItemIngredientDTO:menuItemIngredientDTOS){
            Ingredient ingredient = ingredientRepository.findById(menuItemIngredientDTO.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + menuItemIngredientDTO.getIngredientId()));
            MenuItemIngredient menuItemIngredient = getMenuItemIngredient(menuItemIngredientDTO, menuItem, ingredient);
            menuItemIngredients.add(menuItemIngredient);
        }
        menuItem.setMenuItemIngredients(new HashSet<>(menuItemIngredients));
        for (Category category : categories) {
            category.getMenuItems().add(menuItem);  // Dodaj MenuItem do kategorii
        }
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return MenuItemMapper.toDTO(savedMenuItem);
    }

    private static MenuItemIngredient getMenuItemIngredient(MenuItemIngredientDTO menuItemIngredientDTO, MenuItem menuItem, Ingredient ingredient) {
        MenuItemIngredient menuItemIngredient = new MenuItemIngredient();
        menuItemIngredient.setMenuItem(menuItem);  // Przypisujemy do danego MenuItem
        menuItemIngredient.setIngredient(ingredient);  // Przypisujemy składnik
        menuItemIngredient.setQuantityRequired(menuItemIngredientDTO.getQuantityRequired());
        return menuItemIngredient;
    }

    // Usuń element MenuItem po ID
    public void deleteById(Integer id) {
        menuItemRepository.deleteById(id);
    }

    public MenuItemDTO updateMenuItem(Integer id, MenuItemDTO menuItemDTO) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MenuItem not found"));

        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(menuItemDTO.getCategoryIds()));

        existingMenuItem.setName(menuItemDTO.getName());
        existingMenuItem.setDescription(menuItemDTO.getDescription());
        existingMenuItem.setPrice(menuItemDTO.getPrice());
        existingMenuItem.setCategories(categories);

        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
        return MenuItemMapper.toDTO(updatedMenuItem);
    }
}
