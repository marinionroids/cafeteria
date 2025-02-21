package com.marin.cafeteria.services;


import com.marin.cafeteria.dto.request.EmployeeRegistrationDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.dto.response.UserResponseDTO;
import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }



    public ApiResponse createUser(EmployeeRegistrationDTO employeeRegistrationDTO) {

        // Validates if user exists already.
        if (!validateUserExists(employeeRegistrationDTO)) {

            Employee employee = new Employee();
            employee.setUsername(employeeRegistrationDTO.getUsername());
            employee.setPin(employeeRegistrationDTO.getPin());
            employee.setRole(employeeRegistrationDTO.getRole());
            employee.setActive(false);
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUsername(employee.getUsername());
            userResponseDTO.setRole(employee.getRole());
            userResponseDTO.setActive(employee.isActive());
            employeeRepository.save(employee);


            return new ApiResponse("USER_CREATED", userResponseDTO);

        }

        return new ApiResponse("USER_EXISTS", "Employee already exists");
    }

    private boolean validateUserExists(EmployeeRegistrationDTO dto) {
        return employeeRepository.existsByUsername(dto.getUsername());
    }

}
