package com.marin.cafeteria.services;


import com.marin.cafeteria.dto.request.UserAuthDTO;
import com.marin.cafeteria.dto.request.UserRegistrationDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.dto.response.DuplicateUserException;
import com.marin.cafeteria.dto.response.UserResponseDTO;
import com.marin.cafeteria.model.User;
import com.marin.cafeteria.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    public ApiResponse authUser(UserAuthDTO userAuthDTO) {


    }


    public ApiResponse createUser(UserRegistrationDTO userRegistrationDTO) {

        // Validates if user exists already.
        if (!validateUserExists(userRegistrationDTO)) {

            User user = new User();
            user.setUsername(userRegistrationDTO.getUsername());
            user.setPin(userRegistrationDTO.getPin());
            user.setRole(userRegistrationDTO.getRole());
            user.setActive(false);
            userRepository.save(user);

            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUsername(user.getUsername());
            userResponseDTO.setRole(user.getRole());
            userResponseDTO.setActive(user.isActive());
            return new ApiResponse("USER_CREATED", userResponseDTO);

        }

        return new ApiResponse("USER_EXISTS", "User already exists");
    }

    private boolean validateUserExists(UserRegistrationDTO dto) {
        return userRepository.existsByUsername(dto.getUsername());
    }

}
