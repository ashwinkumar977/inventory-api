package com.rabtech.inventory.controller;

import com.rabtech.inventory.dto.AuthRequestDTO;
import com.rabtech.inventory.dto.AuthResponseDTO;
import com.rabtech.inventory.dto.RegisterRequestDTO;
import com.rabtech.inventory.entity.User;
import com.rabtech.inventory.repository.UserRepository;
import com.rabtech.inventory.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(registerDTO.getRole() != null ? registerDTO.getRole() : "ROLE_USER")
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
}