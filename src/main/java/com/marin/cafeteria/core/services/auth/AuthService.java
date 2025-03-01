package com.marin.cafeteria.core.services.auth;


import com.marin.cafeteria.core.model.auth.Role;
import com.marin.cafeteria.infrastructure.config.jwt.JwtUtil;
import com.marin.cafeteria.api.dto.request.UserAuthDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.model.employee.Employee;
import com.marin.cafeteria.infrastructure.repository.employee.EmployeeRepository;
import jakarta.persistence.EnumType;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;

    public AuthService(EmployeeRepository employeeRepository, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }


    public ApiResponse adminlogin(UserAuthDTO userAuthDTO ) {

        Employee employee = employeeRepository.findByPinAndRole((userAuthDTO.getPin()), Role.valueOf("MANAGER"));
        if (employee == null) {
            return new ApiResponse("LOGIN_FAILED", null);
        }

        return new ApiResponse("LOGIN_SUCCESS", jwtUtil.generateToken(employee));
    }


    public ApiResponse login(UserAuthDTO userAuthDTO) {
        Employee employee = employeeRepository.findByPin((userAuthDTO.getPin()));
        if (employee != null) {
            return new ApiResponse("LOGIN_SUCCESS", jwtUtil.generateToken(employee));
        }

        return new ApiResponse("LOGIN_FAILED", null);
    }
}
