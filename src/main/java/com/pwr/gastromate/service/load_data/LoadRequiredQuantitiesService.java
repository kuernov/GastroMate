package com.pwr.gastromate.service.load_data;

import com.pwr.gastromate.data.MenuItem;
import com.pwr.gastromate.data.MenuItemIngredient;
import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoadRequiredQuantitiesService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    private String extractSizeFromName(String pizzaName) {

        // Example pattern that looks for (S), (M), (L), (XL), (XXL) in the name
        Pattern sizePattern = Pattern.compile("\\((S|M|L|XL|XXL)\\)");
        Matcher matcher = sizePattern.matcher(pizzaName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "S"; // Default to "Small" if no size found
    }

    @Transactional
    public void setQuantities(){
        List<MenuItem> menuItems = menuItemRepository.findAll();
        Map<String, BigDecimal> sizeMultipliers = Map.of(
                "S", new BigDecimal("1"),
                "M", new BigDecimal("1.5"),
                "L", new BigDecimal("2"),
                "XL", new BigDecimal("2.5"),
                "XXL", new BigDecimal("3")
        );
        Map<String, BigDecimal> baseQuantities = Map.ofEntries(
                Map.entry("Zucchini", new BigDecimal("0.15")),
                Map.entry("Barbecue Sauce", new BigDecimal("0.10")),
                Map.entry("Red Peppers", new BigDecimal("0.05")),
                Map.entry("Artichokes", new BigDecimal("0.12")),
                Map.entry("Peperoncini verdi", new BigDecimal("0.03")),
                Map.entry("Oregano", new BigDecimal("0.002")),
                Map.entry("Thai Sweet Chilli Sauce", new BigDecimal("0.08")),
                Map.entry("Bacon", new BigDecimal("0.10")),
                Map.entry("Pancetta", new BigDecimal("0.10")),
                Map.entry("Tomatoes", new BigDecimal("0.20")),
                Map.entry("Italian Sausage", new BigDecimal("0.15")),
                Map.entry("Ricotta Cheese", new BigDecimal("0.12")),
                Map.entry("Luganega Sausage", new BigDecimal("0.12")),
                Map.entry("Capocollo", new BigDecimal("0.08")),
                Map.entry("Fontina Cheese", new BigDecimal("0.10")),
                Map.entry("Mushrooms", new BigDecimal("0.10")),
                Map.entry("Coarse Sicilian Salami", new BigDecimal("0.10")),
                Map.entry("Red Onions", new BigDecimal("0.05")),
                Map.entry("Prosciutto di San Daniele", new BigDecimal("0.07")),
                Map.entry("Calabrese Salami", new BigDecimal("0.08")),
                Map.entry("Anchovies", new BigDecimal("0.02")),
                Map.entry("Brie Carre Cheese", new BigDecimal("0.10")),
                Map.entry("Prosciutto", new BigDecimal("0.07")),
                Map.entry("Beef Chuck Roast", new BigDecimal("0.15")),
                Map.entry("Alfredo Sauce", new BigDecimal("0.10")),
                Map.entry("Arugula", new BigDecimal("0.03")),
                Map.entry("Parmigiano Reggiano Cheese", new BigDecimal("0.08")),
                Map.entry("Chicken", new BigDecimal("0.15")),
                Map.entry("Pears", new BigDecimal("0.10")),
                Map.entry("Gouda Cheese", new BigDecimal("0.10")),
                Map.entry("Goat Cheese", new BigDecimal("0.10")),
                Map.entry("Sun-dried Tomatoes", new BigDecimal("0.05")),
                Map.entry("Pepperoni", new BigDecimal("0.08")),
                Map.entry("Plum Tomatoes", new BigDecimal("0.15")),
                Map.entry("Blue Cheese", new BigDecimal("0.08")),
                Map.entry("Cilantro", new BigDecimal("0.005")),
                Map.entry("Feta Cheese", new BigDecimal("0.08")),
                Map.entry("Jalapeno Peppers", new BigDecimal("0.03")),
                Map.entry("Friggitello Peppers", new BigDecimal("0.03")),
                Map.entry("Eggplant", new BigDecimal("0.15")),
                Map.entry("Pineapple", new BigDecimal("0.20")),
                Map.entry("Spinach", new BigDecimal("0.04")),
                Map.entry("Barbecued Chicken", new BigDecimal("0.15")),
                Map.entry("Caramelized Onions", new BigDecimal("0.05")),
                Map.entry("Provolone Cheese", new BigDecimal("0.10")),
                Map.entry("Genoa Salami", new BigDecimal("0.08")),
                Map.entry("Onions", new BigDecimal("0.05")),
                Map.entry("Green Olives", new BigDecimal("0.03")),
                Map.entry("Kalamata Olives", new BigDecimal("0.03")),
                Map.entry("Asiago Cheese", new BigDecimal("0.08")),
                Map.entry("Smoked Gouda Cheese", new BigDecimal("0.10")),
                Map.entry("Romano Cheese", new BigDecimal("0.08")),
                Map.entry("Pesto Sauce", new BigDecimal("0.08")),
                Map.entry("Corn", new BigDecimal("0.10")),
                Map.entry("Chipotle Sauce", new BigDecimal("0.08")),
                Map.entry("Green Peppers", new BigDecimal("0.05")),
                Map.entry("Chorizo Sausage", new BigDecimal("0.12")),
                Map.entry("Thyme", new BigDecimal("0.002")),
                Map.entry("Mozzarella Cheese", new BigDecimal("0.15")),
                Map.entry("Gorgonzola Piccante Cheese", new BigDecimal("0.08")),
                Map.entry("Artichoke", new BigDecimal("0.12")),
                Map.entry("Garlic", new BigDecimal("0.02")),
                Map.entry("Sliced Ham", new BigDecimal("0.12")),
                Map.entry("Soppressata Salami", new BigDecimal("0.10"))
        );
        for (MenuItem item : menuItems){
            String size = extractSizeFromName(item.getName());
            BigDecimal sizeMultiplier = sizeMultipliers.getOrDefault(size,new BigDecimal("1"));
            Set<MenuItemIngredient> menuItemIngredients = item.getMenuItemIngredients();
            for(MenuItemIngredient itemIngredient : menuItemIngredients){
                BigDecimal baseQuantity = baseQuantities.getOrDefault(itemIngredient.getIngredient().getName(), new BigDecimal("0.1"));
                itemIngredient.setQuantityRequired(baseQuantity.multiply(sizeMultiplier));
            }
            menuItemRepository.save(item);

        }
    }


}
