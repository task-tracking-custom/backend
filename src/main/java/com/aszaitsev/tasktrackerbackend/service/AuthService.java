package com.aszaitsev.tasktrackerbackend.service;

import com.aszaitsev.tasktrackerbackend.service.dto.AuthResponse;
import com.aszaitsev.tasktrackerbackend.service.dto.LoginRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}