package com.marin.cafeteria.infrastructure.repository.employee;


import com.marin.cafeteria.core.model.auth.Role;
import com.marin.cafeteria.core.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByUsername(String username);

    Employee findByUsername(String username);
    Optional<Employee> findById(int id);

    Employee findByPin(int pin);

    Employee findByPinAndRole(int pin, Role role);

    List<Employee> Role(Role role);
}
