package com.pwr.gastromate.service.load_data;
import com.pwr.gastromate.data.*;
import com.pwr.gastromate.repository.MenuItemRepository;
import com.pwr.gastromate.repository.OrderRepository;
import com.pwr.gastromate.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class LoadOrdersService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void loadOrdersFromExcel(String filePath) {
        Map<Integer, Order> orderMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("pizza_sales"); // Przyjmujemy, że dane zamówienia są w arkuszu o nazwie "orders"
            for (Row row : sheet) {
                // Pomijanie pierwszego wiersza (nagłówków)
                if (row.getRowNum() == 0) continue;

                // Pobieranie danych zamówienia
                Integer orderId = (int) row.getCell(1).getNumericCellValue(); // Kolumna B - order_id
                // Pobieranie daty zamówienia (kolumna E - order_date)
                Date orderDateCell = row.getCell(4).getDateCellValue(); // Kolumna E - order_date (np. "2023-10-25")
                LocalDate orderDate = orderDateCell.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // Pobieranie czasu zamówienia (kolumna F - order_time)
                LocalDateTime orderTime = row.getCell(5).getLocalDateTimeCellValue(); // Kolumna F - order_time (np. "15:30:45")
                LocalTime orderTimeOnly = orderTime.toLocalTime();

                // Połączenie daty i czasu w LocalDateTime
                LocalDateTime combinedDateTime = LocalDateTime.of(orderDate, orderTimeOnly);

                // Konwersja na Timestamp
                Timestamp orderTimestamp = Timestamp.valueOf(combinedDateTime);

                // Tworzenie lub znalezienie użytkownika
                User user = userRepository.findById(1)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + 1));

                Order order;
                if (orderMap.containsKey(orderId)) {
                    order = orderMap.get(orderId);
                } else {
                    order = new Order();
                    order.setOrderDate(orderTimestamp);
                    order.setUser(user);
                    order.setOrderItems(new ArrayList<>());
                    orderMap.put(orderId, order);
                    orderRepository.save(order);
                }

                String pizzaName = row.getCell(11).getStringCellValue() +" ("+row.getCell(8).getStringCellValue()+")";
                MenuItem menuItem = menuItemRepository.findByName(pizzaName);
                if (menuItem == null) {
                    throw new IllegalArgumentException("Menu item not found with name: " + pizzaName);
                }
                int quantity = (int) row.getCell(3).getNumericCellValue(); // Kolumna D - quantity
                BigDecimal priceAtOrder = BigDecimal.valueOf(row.getCell(6).getNumericCellValue()); // Kolumna H - priceAtOrder


                // Tworzenie szczegółu zamówienia (OrderItem)
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(menuItem);
                orderItem.setOrder(order);
                orderItem.setPriceAtOrder(priceAtOrder);
                orderItem.setQuantity(quantity);

                // Ustawienie klucza złożonego w OrderItem
                OrderItemId orderItemId = new OrderItemId();
                orderItemId.setMenuItemId(menuItem.getId());
                orderItemId.setOrderId(order.getId());
                orderItem.setId(orderItemId);

                // Dodawanie OrderItem do zamówienia
                order.getOrderItems().add(orderItem);

            }
            orderRepository.saveAll(orderMap.values());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
