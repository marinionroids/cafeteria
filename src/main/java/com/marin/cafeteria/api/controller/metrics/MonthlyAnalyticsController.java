package com.marin.cafeteria.api.controller.metrics;


import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.api.dto.response.MonthlyAnalyticsResponse;
import com.marin.cafeteria.core.services.metrics.MonthlyAnalyticsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
// ADMIN ONLY CONTROLLER
@RequestMapping("/api/admin")
public class MonthlyAnalyticsController {

    private final MonthlyAnalyticsService monthlyAnalyticsService;

    public MonthlyAnalyticsController(MonthlyAnalyticsService monthlyAnalyticsService) {
        this.monthlyAnalyticsService = monthlyAnalyticsService;
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getMonthlyAnalytics(@RequestParam("month")  @Min(1) @Max(12) int month,
                                                @RequestParam("year") int year) {

        ApiResponse response = monthlyAnalyticsService.getMonthlyAnalytics(month, year);

        return ResponseEntity.ok(response);
    }
}
