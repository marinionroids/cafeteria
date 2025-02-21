package com.marin.cafeteria.services;


import com.marin.cafeteria.config.jwt.JwtUtil;
import com.marin.cafeteria.dto.request.UserAuthDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;

    public AuthService(EmployeeRepository employeeRepository, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse login(UserAuthDTO userAuthDTO) {
        Employee employee = employeeRepository.findByPin((userAuthDTO.getPin()));
        if (employee != null) {
            return new ApiResponse("LOGIN_SUCCESS", jwtUtil.generateToken(employee));
        }

        return new ApiResponse("LOGIN_FAILED", null);
    }
}
