package com.marin.cafeteria.controller;


import com.marin.cafeteria.dto.request.UserRegistrationDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.User;
import com.marin.cafeteria.services.UserService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO dto) {
        ApiResponse response = userService.createUser(dto);
        if (response.getStatus().equals("USER_CREATED")) {
            return ResponseEntity
                    .ok()
                    .body(response);
        }

        // User exists
        return ResponseEntity
                .badRequest()
                .body(response);

    }

    @GetMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam int pin) {

    }
}
