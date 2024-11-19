package com.pwr.gastromate.prediction;

import com.pwr.gastromate.data.Order;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.DailyIngredientUsageDTO;
import com.pwr.gastromate.dto.PredictionResultDTO;
import com.pwr.gastromate.dto.WeeklyAverageUsageDTO;
import com.pwr.gastromate.exception.UnauthorizedException;
import com.pwr.gastromate.prediction.ArimaPredictionService;
import com.pwr.gastromate.repository.OrderRepository;
import com.pwr.gastromate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class PredictionController {

    @Autowired
    private ArimaPredictionService arimaPredictionService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    private User findUser(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthorized: Principal is null");
        }
        String username = principal.getName();
        return userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @GetMapping("/predict-ingredient-usage")
    public ResponseEntity<List<PredictionResultDTO>> predictIngredientUsage(
            @RequestParam Integer ingredientId,
            @RequestParam boolean autoTune,
            @RequestParam int seasonalPeriod,
            @RequestParam int steps,
            @RequestParam(required = false) Integer p,
            @RequestParam(required = false) Integer d,
            @RequestParam(required = false) Integer q,
            @RequestParam(required = false) Integer P,
            @RequestParam(required = false) Integer D,
            @RequestParam(required = false) Integer Q) {


        try {
            List<PredictionResultDTO> predictions = arimaPredictionService.predictIngredientUsage(ingredientId, steps, autoTune);
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception and return an appropriate response
            return ResponseEntity.status(500).body(List.of()); // Return 500 Internal Server Error with empty list
        }
    }


    @GetMapping("/weekly-average-usage")
    public ResponseEntity<List<WeeklyAverageUsageDTO>> getWeeklyAverage(){
        List<Object[]> results = orderRepository.findAverageUsageByDayOfWeekNative(1);

        // Mapowanie wyników na List<WeeklyAverageUsageDTO>
        List<WeeklyAverageUsageDTO> list = results.stream()
                .map(row -> new WeeklyAverageUsageDTO(
                        ((Number) row[0]).intValue(),   // day_of_week
                        ((Number) row[1]).doubleValue() // average_usage
                ))
                .toList();
        return ResponseEntity.ok(list);
    }
}
