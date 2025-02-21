package com.marin.cafeteria.config.jwt;


import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.repository.EmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;

    public JwtFilter(final JwtUtil jwtUtil, final EmployeeRepository employeeRepository) {
        this.jwtUtil = jwtUtil;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                // Remove "Bearer " prefix
                String token = header.substring(7);

                // Validate token and get username
                String username = jwtUtil.validateToken(token);

                if (username != null) {
                    Employee employee = employeeRepository.findByUsername(username);

                    if (employee != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(employee, null,
                                        List.of(new SimpleGrantedAuthority(employee.getRole().toString())));

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        } catch (Exception e) {
            // Log the error but don't stop the request
            System.out.println("Error validating token: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}