package com.pwr.gastromate.prediction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwr.gastromate.dto.DailyIngredientUsageDTO;
import com.pwr.gastromate.dto.PredictionResultDTO;
import com.pwr.gastromate.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ArimaPredictionService {

    @Autowired
    private OrderRepository orderRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String sarimaApiUrl = "http://sarima-predict:5000/predict";


    public List<PredictionResultDTO> predictIngredientUsage(Integer ingredientId, int steps, boolean autoTune, Integer p, Integer q, Integer d, Integer P, Integer Q, Integer D) {
        try {
            List<Object[]> usageData = orderRepository.findDailyIngredientUsage(ingredientId);

            List<DailyIngredientUsageDTO> dailyUsage = usageData.stream()
                    .map(row -> new DailyIngredientUsageDTO(
                            ((Date) row[0]).toLocalDate(), // Konwersja java.sql.Date do LocalDate
                            ((BigDecimal) row[1])
                    ))
                    .toList();

            Map<LocalDate, BigDecimal> usageMap = new TreeMap<>();
            for (DailyIngredientUsageDTO dto : dailyUsage) {
                usageMap.put(dto.getOrderDate(), dto.getDailyConsumption());
            }

            if (!usageMap.isEmpty()) {
                LocalDate startDate = usageMap.keySet().iterator().next();
                LocalDate endDate = usageMap.keySet().stream().reduce((first, second) -> second).orElse(startDate);

                LocalDate current = startDate;
                while (!current.isAfter(endDate)) {
                    usageMap.putIfAbsent(current, BigDecimal.ZERO);
                    current = current.plusDays(1);
                }
            }
            List<Double> data = usageMap.values().stream()
                    .limit(Math.max(0, usageMap.size() - 31))
                    .map(BigDecimal::doubleValue)
                    .toList();

            Map<String, Object> requestBody = Map.of(
                    "data", data,
                    "auto_tune", autoTune,
                    "seasonal_period", 7,
                    "steps", steps,
                    "p", p,
                    "q", q,
                    "d", d,
                    "P", P,
                    "Q", Q,
                    "D", D
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(sarimaApiUrl, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
                List<Double> forecast = (List<Double>) responseMap.get("forecast");

                LocalDate lastDate = dailyUsage.isEmpty()
                        ? LocalDate.now()
                        : dailyUsage.get(dailyUsage.size() - 1).getOrderDate();

                List<PredictionResultDTO> results = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                for (int i = 0; i < forecast.size(); i++) {
                    LocalDate predictionDate = lastDate.plusDays(i + 1);
                    results.add(new PredictionResultDTO(predictionDate.format(formatter), forecast.get(i)));
                }

                return results;

            } else {
                throw new RuntimeException("Error during prediction: " + response.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to predict ingredient usage", e);
        }
    }

}


