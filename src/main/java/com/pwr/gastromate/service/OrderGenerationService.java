package com.pwr.gastromate.service;

import com.github.javafaker.Faker;
import com.pwr.gastromate.data.MenuItem;
import com.pwr.gastromate.data.OrderItem;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.OrderRepository;
import com.pwr.gastromate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class OrderGenerationService {


    private final OrderRepository orderRepository;

    private final MenuItemRepository menuItemRepository;

    private final UserRepository userRepository;

    private final Faker faker = new Faker();

    public void generateOrders(int numberOfOrders, User user) {
        Random random = new Random();
        List<MenuItem> menuItems = menuItemRepository.findByUserId(user.getId());

        for (int i = 0; i < numberOfOrders; i++) {
            // Tworzenie nowego zamówienia
            Order order = new Order();
            order.setUser(user); // Losowy użytkownik

            Timestamp orderDate = new Timestamp(faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).getTime()); // Losowa data z ostatniego roku
            order.setOrderDate(orderDate);

            // Dodawanie elementów zamówienia
            List<OrderItem> items = new ArrayList<>();
            int numberOfItems = random.nextInt(5) + 1; // Losowa liczba produktów
            for (int j = 0; j < numberOfItems; j++) {
                MenuItem randomMenuItem = menuItems.get(random.nextInt(menuItems.size()));

                // Tworzenie OrderItem
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(randomMenuItem);
                orderItem.setOrder(order);
                orderItem.setQuantity(faker.number().numberBetween(1, 5)); // Losowa ilość

                items.add(orderItem);
            }
            order.setOrderItems(items);

            // Zapis do bazy danych
            orderRepository.save(order);
        }
    }
}
