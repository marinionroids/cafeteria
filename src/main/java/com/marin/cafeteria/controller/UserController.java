package com.marin.cafeteria.controller;


import com.marin.cafeteria.dto.request.UserRegistrationDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        ApiResponse response = userService.createUser(userRegistrationDTO);
        if (response.getStatus().equals("USER_CREATED")) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
