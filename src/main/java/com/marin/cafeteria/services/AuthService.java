package com.marin.cafeteria.services;


import com.marin.cafeteria.config.jwt.JwtUtil;
import com.marin.cafeteria.dto.request.UserAuthDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.User;
import com.marin.cafeteria.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse login(UserAuthDTO userAuthDTO) {
        User user = userRepository.findByUsername(userAuthDTO.getUsername());
        if (user != null && user.getPin() == userAuthDTO.getPin()) {
            return new ApiResponse("LOGIN_SUCCESS", jwtUtil.generateToken(user));
        }

        return new ApiResponse("LOGIN_FAILED", null);
    }
}
