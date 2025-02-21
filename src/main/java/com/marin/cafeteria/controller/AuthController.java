package com.marin.cafeteria.controller;


import com.marin.cafeteria.dto.request.UserAuthDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;
    }


    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthDTO userAuthDTO){

        ApiResponse response = authService.login(userAuthDTO);
        if (response.getStatus().equals("LOGIN_SUCCESS")) {
            return ResponseEntity.ok()
                    .body(response);
        }

        return ResponseEntity
                .badRequest()
                .body(response);

    }
}






















