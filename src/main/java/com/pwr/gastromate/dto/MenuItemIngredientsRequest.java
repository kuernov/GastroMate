package com.pwr.gastromate.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
public class MenuItemIngredientsRequest {
    private List<Integer> ingredientIds;  // Oczekujemy tablicy/liczby całkowitej

}
