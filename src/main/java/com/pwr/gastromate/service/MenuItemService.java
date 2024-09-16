package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.data.MenuItem;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.MenuItemDTO;
import com.pwr.gastromate.repository.CategoryRepository;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.UserRepository;
import com.pwr.gastromate.service.mapper.MenuItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

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
    public MenuItemDTO save(MenuItemDTO menuItemDTO) {
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(menuItemDTO.getCategoryIds()));
        User user = userRepository.findById(menuItemDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MenuItem menuItem = MenuItemMapper.toEntity(menuItemDTO, categories, user);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return MenuItemMapper.toDTO(savedMenuItem);
    }

    // Usuń element MenuItem po ID
    public void deleteById(Integer id) {
        menuItemRepository.deleteById(id);
    }

    public MenuItemDTO updateMenuItem(Integer id, MenuItemDTO menuItemDTO) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MenuItem not found"));

        Set<Category> categories = categoryRepository.findAllById(menuItemDTO.getCategoryIds()).stream().collect(Collectors.toSet());
        User user = userRepository.findById(menuItemDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingMenuItem.setName(menuItemDTO.getName());
        existingMenuItem.setDescription(menuItemDTO.getDescription());
        existingMenuItem.setPrice(menuItemDTO.getPrice());
        existingMenuItem.setCategories(categories);
        existingMenuItem.setUser(user);

        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
        return MenuItemMapper.toDTO(updatedMenuItem);
    }
}
