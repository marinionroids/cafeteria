package com.marin.cafeteria.api.controller.metrics;

import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.services.metrics.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> getMetrics(@RequestHeader("Authorization") String token) {

        ApiResponse response = metricsService.getMetrics(token);
        return ResponseEntity.ok(response);
    }
}
