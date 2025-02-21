package com.marin.cafeteria.controller;


import com.marin.cafeteria.dto.request.EmployeeRegistrationDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody EmployeeRegistrationDTO employeeRegistrationDTO) {
        ApiResponse response = employeeService.createUser(employeeRegistrationDTO);
        if (response.getStatus().equals("USER_CREATED")) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
