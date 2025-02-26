package com.marin.cafeteria.api.controller.employee;


import com.marin.cafeteria.api.dto.request.EmployeeRegistrationDTO;
import com.marin.cafeteria.api.dto.request.ServerOrdersRequestDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.services.employee.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping("/admin/register")
    public ResponseEntity<?> createUser(@RequestBody EmployeeRegistrationDTO employeeRegistrationDTO) {
        ApiResponse response = employeeService.createUser(employeeRegistrationDTO);
        if (response.getStatus().equals("USER_CREATED")) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }


    @GetMapping("/admin/staff")
    public ResponseEntity<?> getAllServers() {
        ApiResponse response = employeeService.getAllServers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/server-orders")
    public ResponseEntity<?> getAllServerOrders(@ModelAttribute ServerOrdersRequestDTO dto) {
        ApiResponse response = employeeService.getServerOrders(dto);
        return ResponseEntity.ok(response);
    }

}
