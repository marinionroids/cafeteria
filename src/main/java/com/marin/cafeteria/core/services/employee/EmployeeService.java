package com.marin.cafeteria.core.services.employee;


import com.marin.cafeteria.api.dto.request.EmployeeRegistrationDTO;
import com.marin.cafeteria.api.dto.request.ServerOrdersRequestDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.api.dto.response.UserResponseDTO;
import com.marin.cafeteria.core.model.employee.Employee;
import com.marin.cafeteria.core.model.order.Order;
import com.marin.cafeteria.infrastructure.repository.employee.EmployeeRepository;
import com.marin.cafeteria.infrastructure.repository.order.OrderRepository;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;

    public EmployeeService(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
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

    public ApiResponse getAllServers(){
        List<Employee> employees = employeeRepository.findAll();
        return new ApiResponse("EMPLOYEE_LIST", employees);
    }

    public ApiResponse getServerOrders(ServerOrdersRequestDTO dto){

        Employee employee = employeeRepository.findById(dto.getServerId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Order> orders = new ArrayList<>();
        if (dto.getStartDate() == null && dto.getEndDate() != null) {
            // Gets all the orders from the beggining to the specified endDate.
            LocalDateTime startDate = LocalDateTime.of(2024,12,31,0,0);
            orders = orderRepository.findByOrderTimeBetweenAndServer(Timestamp.valueOf(startDate), dto.getEndDate(), employee);
        }
        if (dto.getStartDate() != null && dto.getEndDate() == null) {
            // Gets all the orders from the specified start date to the latest one.
            LocalDateTime endDate = LocalDateTime.now().with(LocalTime.MAX);
            orders = orderRepository.findByOrderTimeBetweenAndServer(dto.getStartDate(), Timestamp.valueOf(endDate), employee);
        }
        if (dto.getStartDate() == null && dto.getEndDate() == null) {
            // Gets all the orders ever of a server.
            LocalDateTime startDate = LocalDateTime.of(2024,12,31,0,0);
            LocalDateTime endDate = LocalDateTime.now().with(LocalTime.MAX);
            orders = orderRepository.findByOrderTimeBetweenAndServer(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), employee);
        }
        else if (dto.getStartDate() != null && dto.getEndDate() != null) {
            orders = orderRepository.findByOrderTimeBetweenAndServer(dto.getStartDate(), dto.getEndDate(), employee);
        }

        return new ApiResponse("SERVER_ORDERS", orders);




    }


}
