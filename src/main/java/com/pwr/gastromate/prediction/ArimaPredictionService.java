package com.pwr.gastromate.prediction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwr.gastromate.dto.DailyIngredientUsageDTO;
import com.pwr.gastromate.dto.PredictionResultDTO;
import com.pwr.gastromate.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArimaPredictionService {

    @Autowired
    private OrderRepository orderRepository;


//    public List<PredictionResultDTO> predictIngredientUsage(Integer ingredientId,  boolean autoTune, int seasonalPeriod, int steps,
//                                                            Integer p, Integer d, Integer q, Integer P, Integer D, Integer Q) throws Exception {
//        List<DailyIngredientUsageDTO> usageData = orderRepository.findDailyIngredientUsage(ingredientId);
//
//        List<Double> data = usageData.stream()
//                .map(dto -> dto.getDailyConsumption().doubleValue())
//                .limit(Math.max(0, usageData.size() - 61))
//                .collect(Collectors.toList());
//
//        List<Double> predictions = predict(data, p, d, q, P, D, Q, s, steps);
//
//        // Zakładamy, że prognozy dotyczą kolejnych dni po ostatniej dacie w danych
//        LocalDate lastDate = usageData.isEmpty() ? LocalDate.now() : usageData.get(usageData.size() - 1).getOrderDate().toLocalDate().minusMonths(2);;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // Tworzymy listę wyników z datami
//        List<PredictionResultDTO> results = new ArrayList<>();
//        for (int i = 0; i < predictions.size(); i++) {
//            LocalDate predictionDate = lastDate.plusDays(i + 1);
//            results.add(new PredictionResultDTO(predictionDate.format(formatter), predictions.get(i)));
//        }
//
//        return results;
//    }
//
//    private List<Double> predict(List<Double> data, int p, int d, int q, int P, int D, int Q, int s, int steps) throws Exception {
//        JSONArray jsonData = new JSONArray(data);
//
//        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/sarima_predict.py",
//                jsonData, String.valueOf(autoTune),
//                String.valueOf(seasonalPeriod), String.valueOf(steps));
//
//        if (!autoTune) {
//            processBuilder.command().add(String.valueOf(p));
//            processBuilder.command().add(String.valueOf(d));
//            processBuilder.command().add(String.valueOf(q));
//            processBuilder.command().add(String.valueOf(P));
//            processBuilder.command().add(String.valueOf(D));
//            processBuilder.command().add(String.valueOf(Q));
//        }
//
//        Process process = processBuilder.start();
//
//        // Wczytaj cały wynik ze standardowego wyjścia
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//
//            // Odczytaj wszystkie linie wyjścia i połącz je w jeden ciąg znaków
//            String output = reader.lines().collect(Collectors.joining("\n"));
//            System.out.println("Python script output: " + output);
//
//            // Sprawdź, czy wynik jest poprawnym JSON-em
//            try {
//                JSONArray jsonResult = new JSONArray(output);
//                return jsonResult.toList().stream()
//                        .map(o -> ((Number) o).doubleValue())
//                        .toList();
//            } catch (org.json.JSONException e) {
//                // Obsłuż przypadek, gdy wynik nie jest poprawnym JSON-em
//                String errorOutput = errorReader.lines().collect(Collectors.joining("\n"));
//                System.err.println("Python script error: " + errorOutput);
//                throw new RuntimeException("Python script returned non-JSON output: " + output, e);
//            }
//        }
//    }

    public List<PredictionResultDTO> predictDemand(Integer ingredientId, boolean autoTune, int seasonalPeriod, int steps,
                                                   Integer p, Integer d, Integer q, Integer P, Integer D, Integer Q) throws Exception {
        List<DailyIngredientUsageDTO> usageData = orderRepository.findDailyIngredientUsage(ingredientId);

        List<Double> data = usageData.stream()
                .map(dto -> dto.getDailyConsumption().doubleValue())
                .limit(Math.max(0, usageData.size() - 32))
                .toList();
        // Convert data to JSON format
        String jsonData = new ObjectMapper().writeValueAsString(data);
        System.out.println("JSON data for SARIMA: " + jsonData);
        Path tempFile = Files.createTempFile("sarima_input", ".json");
        Files.write(tempFile, jsonData.getBytes(StandardCharsets.UTF_8));
        String fileContent = Files.readString(tempFile);
        System.out.println("File content written: " + fileContent);
        // Build command to execute the Python script
        ProcessBuilder processBuilder = new ProcessBuilder(
                "python", "src/main/resources/sarima_predict.py",
                tempFile.toString(), String.valueOf(autoTune),
                String.valueOf(seasonalPeriod), String.valueOf(steps)
        );

        // Add SARIMA parameters if auto-tuning is disabled
        if (!autoTune) {
            processBuilder.command().add(String.valueOf(p));
            processBuilder.command().add(String.valueOf(d));
            processBuilder.command().add(String.valueOf(q));
            processBuilder.command().add(String.valueOf(P));
            processBuilder.command().add(String.valueOf(D));
            processBuilder.command().add(String.valueOf(Q));
        }

// Start the process
        Process process = processBuilder.start();

// Read the Python script output
        List<Double> predictions;
        List<Integer> selectedOrder;
        List<Integer> selectedSeasonalOrder;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String output = reader.lines().collect(Collectors.joining());

            // Parse the JSON output into a Map
            Map<String, Object> resultMap = new ObjectMapper().readValue(output, Map.class);
            // Extract the forecast, selected order, and seasonal order
            predictions = (List<Double>) resultMap.get("forecast");
            selectedOrder = (List<Integer>) resultMap.get("selected_order");
            selectedSeasonalOrder = (List<Integer>) resultMap.get("selected_seasonal_order");
        }

// You can now log or print the selected parameters
        System.out.println("Selected order: " + selectedOrder);
        System.out.println("Selected seasonal order: " + selectedSeasonalOrder);


// Determine the last date and format predictions with dates
        LocalDate lastDate = usageData.isEmpty() ? LocalDate.now() : usageData.get(usageData.size() - 32).getOrderDate().toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Create a list of PredictionResultDTO with formatted dates and predictions
        List<PredictionResultDTO> results = new ArrayList<>();
        for (int i = 0; i < predictions.size(); i++) {
            LocalDate predictionDate = lastDate.plusDays(i + 1);
            results.add(new PredictionResultDTO(predictionDate.format(formatter), predictions.get(i)));
        }

        return results;

    }

}


