package com.marin.cafeteria.controller;

import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.services.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getReports(@RequestHeader("Authorization") String token) {

        ApiResponse response = reportsService.getReports(token);
        return ResponseEntity.ok(response);
    }
}
