package com.marin.cafeteria.dto.response;

import com.marin.cafeteria.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String username;
    private Role role;
    private boolean active;
}
