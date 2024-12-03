package com.pwr.gastromate.service;

import com.pwr.gastromate.data.DeliveryStatus;
import com.pwr.gastromate.data.Ingredient;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.data.supplyOrder.SupplyOrder;
import com.pwr.gastromate.data.supplyOrder.SupplyOrderItem;
import com.pwr.gastromate.data.supplyOrder.SupplyOrderItemId;
import com.pwr.gastromate.dto.SupplyOrderItemRequest;
import com.pwr.gastromate.dto.SupplyOrderRequest;
import com.pwr.gastromate.repository.IngredientRepository;
import com.pwr.gastromate.repository.SupplyOrderItemRepository;
import com.pwr.gastromate.repository.SupplyOrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SupplyOrderService {

    private final SupplyOrderRepository supplyOrderRepository;
    private final SupplyOrderItemRepository supplyOrderItemRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional
    public void createSupplyOrder(SupplyOrderRequest supplyOrderItemRequests, User user) {
        SupplyOrder supplyOrder = new SupplyOrder();
        supplyOrder.setUser(user);
        supplyOrder.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        supplyOrder.setDeliveryDate(Date.valueOf(LocalDate.now().plusDays(3)));
        supplyOrder.setStatus(DeliveryStatus.Pending);
        supplyOrderRepository.save(supplyOrder);
        List<SupplyOrderItem> itemList = new ArrayList<>();
        for (SupplyOrderItemRequest request : supplyOrderItemRequests.getSupplyOrderItemRequestList()) {
            Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found: " + request.getIngredientId()));
            SupplyOrderItemId itemId = new SupplyOrderItemId();
            itemId.setSupplyOrderId(supplyOrder.getId());
            itemId.setIngredientId(ingredient.getId());

            SupplyOrderItem item = new SupplyOrderItem();
            item.setId(itemId);
            item.setSupplyOrder(supplyOrder);
            item.setIngredient(ingredient);
            item.setQuantity(request.getQuantity());
            itemList.add(item);
        }
        supplyOrderItemRepository.saveAll(itemList);
        supplyOrder.setSupplyOrderItems(itemList);
        supplyOrderRepository.save(supplyOrder);

    }
}
