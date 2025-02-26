package com.marin.cafeteria.api.controller.auth;


import com.marin.cafeteria.api.dto.request.UserAuthDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.services.auth.AuthService;
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






















