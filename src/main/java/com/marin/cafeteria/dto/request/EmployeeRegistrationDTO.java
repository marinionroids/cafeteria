package com.marin.cafeteria.dto.request;

import com.marin.cafeteria.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegistrationDTO {
    private String username;
    private int pin;
    private Role role;


}
