package com.pwr.gastromate.service;

import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.restock.MissingIngredientDTO;
import com.pwr.gastromate.dto.restock.MissingItemsResponse;
import com.pwr.gastromate.dto.restock.StockStatus;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.OrderItemRepository;
import com.pwr.gastromate.service.mapper.IngredientMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class MissingItemsService {
    private final IngredientRepository ingredientRepository;
    private final OrderItemRepository orderItemRepository;
    private final IngredientMapper ingredientMapper;
    private static final double MINIMUM_THRESHOLD = 0.9;

    private static final double CRITICAL_THRESHOLD = 0.7;


    public MissingItemsResponse findLowStockIngredients(User user) {
        List<Object[]> results = orderItemRepository.findIngredientsAndAverageUsageByUserId(user.getId());
        MissingItemsResponse response = new MissingItemsResponse();

        List<MissingIngredientDTO> items = new ArrayList<>();

        for (Object[] row : results) {
            Ingredient ingredient = ingredientRepository.findById((Integer) row[0]).orElseThrow();
            BigDecimal averageUsageDecimal = (BigDecimal) row[1];
            double averageUsage = averageUsageDecimal != null ? averageUsageDecimal.doubleValue() : 0.0;
            BigDecimal quantitySum = ingredientRepository.sumQuantityByNameAndUserId(ingredient.getName(),user.getId());
            BigDecimal difference = quantitySum.subtract(BigDecimal.valueOf(averageUsage));

            StockStatus status;
            if (ingredient.getQuantity() < averageUsage * CRITICAL_THRESHOLD) {
                status = StockStatus.CRITICAL;
            } else if (ingredient.getQuantity() < averageUsage * MINIMUM_THRESHOLD) {
                status = StockStatus.LOW_STOCK;
            } else {
                status = StockStatus.NORMAL;
            }
            items.add(new MissingIngredientDTO(
                    ingredientMapper.toDTO(ingredient),
                    quantitySum,
                    averageUsage,
                    difference,
                    status
            ));
        }

        items.sort(Comparator
                .comparing(MissingIngredientDTO::getStatus) // Sortowanie po statusie
                .thenComparingDouble(missingIngredientDTO -> missingIngredientDTO.getDifference().doubleValue()));
        response.getMissingItems().addAll(items);

        return response;
    }
}
