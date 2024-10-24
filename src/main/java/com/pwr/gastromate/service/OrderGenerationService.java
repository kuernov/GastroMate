package com.pwr.gastromate.service;

import com.github.javafaker.Faker;
import com.pwr.gastromate.data.MenuItem;
import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.OrderItemRepository;
import com.pwr.gastromate.repository.OrderRepository;
import com.pwr.gastromate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderGenerationService {


    private final OrderRepository orderRepository;

    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    private final Faker faker = new Faker();

    public void generateOrders(int numberOfOrders, User user) {
        Random random = new Random();
        List<MenuItem> menuItems = menuItemRepository.findByUserId(user.getId());

        for (int i = 0; i < numberOfOrders; i++) {
            // Tworzenie nowego zamówienia
            Order order = new Order();
            order.setUser(user);


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(faker.date().past(365, java.util.concurrent.TimeUnit.DAYS));

// Ustawienie losowej godziny w widełkach (np. 8:00 - 18:00)
            int minHour = 8;   // minimalna godzina
            int maxHour = 22;  // maksymalna godzina
            int randomHour = minHour + (int) (Math.random() * ((maxHour - minHour) + 1));

            calendar.set(Calendar.HOUR_OF_DAY, randomHour);

// Jeśli chcesz ustawić też losowe minuty i sekundy
            int randomMinute = (int) (Math.random() * 60);  // losowe minuty (0-59)
            int randomSecond = (int) (Math.random() * 60);  // losowe sekundy (0-59)

            calendar.set(Calendar.MINUTE, randomMinute);
            calendar.set(Calendar.SECOND, randomSecond);
            calendar.set(Calendar.MILLISECOND, 0);  // ustawiamy milisekundy na 0
            Date randomDate = calendar.getTime();
            Timestamp orderDate = new Timestamp(randomDate.getTime());
            order.setOrderDate(orderDate);
            orderRepository.save(order);
            // Dodawanie elementów zamówienia
            List<OrderItem> items = new ArrayList<>();
            List<MenuItem> availableMenuItems = new ArrayList<>(menuItems); // Kopia oryginalnej listy
            int numberOfItems = random.nextInt(5) + 1; // Losowa liczba produktów
            for (int j = 0; j < numberOfItems; j++) {
                if (availableMenuItems.isEmpty()) {
                    break;
                }
                int randomIndex = random.nextInt(availableMenuItems.size());
                MenuItem randomMenuItem = availableMenuItems.get(randomIndex);
                availableMenuItems.remove(randomIndex);

                // Tworzenie OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(randomMenuItem);
                orderItem.setOrder(order);
                orderItem.setPriceAtOrder(randomMenuItem.getPrice());
                orderItem.setQuantity(faker.number().numberBetween(1, 5)); // Losowa ilość
                items.add(orderItem);
            }

            order.setOrderItems(items);
            // Zapis do bazy danych
            orderRepository.save(order);
        }
    }
}
