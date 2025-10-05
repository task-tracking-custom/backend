package com.aszaitsev.tasktrackerbackend.service.impl;

import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.service.AuthService;
import com.aszaitsev.tasktrackerbackend.service.JwtService;
import com.aszaitsev.tasktrackerbackend.service.UserService;
import com.aszaitsev.tasktrackerbackend.service.dto.response.AuthResponse;
import com.aszaitsev.tasktrackerbackend.service.dto.request.LoginRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = userService.registerUser(request);
        
        String token = jwtService.generateTokenFromUsername(user.getUsername());
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }
    
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtService.generateToken(authentication);
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setUsername(request.getUsername());
        return response;
    }
}