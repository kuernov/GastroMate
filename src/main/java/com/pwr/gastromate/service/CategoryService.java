package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.CategoryDTO;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.CategoryRepository;
import com.pwr.gastromate.repository.UserRepository;
import com.pwr.gastromate.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CategoryDTO> getAllCategoriesByUserId(Integer userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO findById(Integer id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryMapper.toDTO(category);
    }

    public CategoryDTO createCategoryForUser(CategoryDTO categoryDTO, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = CategoryMapper.toEntity(categoryDTO);
        category.setUser(user);  // Powiązanie kategorii z użytkownikiem
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDTO(savedCategory);
    }

    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id: " + id));

        categoryRepository.delete(category);
    }
}