package com.marin.cafeteria.api.controller.metrics;

import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.services.metrics.MetricsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> getMetrics(@RequestHeader("Authorization") String token, @RequestParam("day") @Min(1) @Max(31) int day,@RequestParam("month")  @Min(1) @Max(12) int month,
                                        @RequestParam("year") int year) {

        ApiResponse response = metricsService.getMetrics(day, month, year, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/servers-balance")
    public ResponseEntity<?> getServersBalance(@RequestParam("day") @Min(1) @Max(31) int day,@RequestParam("month")  @Min(1) @Max(12) int month,
                                               @RequestParam("year") int year) {
        ApiResponse response = metricsService.getAllServersBalance(day, month, year);
        return ResponseEntity.ok(response);
    }
}
