package com.disaster.user.service;

import com.disaster.user.dto.LoginRequest;
import com.disaster.user.dto.RegisterRequest;
import com.disaster.user.dto.UserResponse;
import com.disaster.user.entity.Role;
import com.disaster.user.entity.User;
import com.disaster.user.exception.DuplicateEmailException;
import com.disaster.user.exception.InvalidCredentialsException;
import com.disaster.user.exception.ResourceNotFoundException;
import com.disaster.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered: " + request.getEmail());
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // NOTE: for a production system this password must be hashed (e.g. BCrypt).
        // Kept as plain text here to keep the fresher-project scope manageable.
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user);
        return UserResponse.fromEntity(saved);
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return UserResponse.fromEntity(user);
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserResponse.fromEntity(user);
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
