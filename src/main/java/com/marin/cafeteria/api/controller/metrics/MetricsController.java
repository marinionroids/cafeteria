package com.marin.cafeteria.api.controller.metrics;

import com.marin.cafeteria.api.dto.request.TimeDTO;
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
    public ResponseEntity<?> getMetrics(@RequestHeader("Authorization") String token, @ModelAttribute TimeDTO timeDTO) {

        ApiResponse response = metricsService.getMetrics(timeDTO.getDay(), timeDTO.getMonth(), timeDTO.getYear(), token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/servers-balance")
    public ResponseEntity<?> getServersBalance(@ModelAttribute TimeDTO timeDTO) {
        ApiResponse response = metricsService.getAllServersBalance(timeDTO.getDay(), timeDTO.getMonth(), timeDTO.getYear());
        return ResponseEntity.ok(response);
    }
}
