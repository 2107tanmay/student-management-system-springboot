package com.example.student_management_sys_demo.controllers;

import com.example.student_management_sys_demo.dto.AuthRequest;
import com.example.student_management_sys_demo.dto.AuthResponse;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.UserRepository;
import com.example.student_management_sys_demo.security.JwtTokenProvider;
import java.util.NoSuchElementException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }
}
