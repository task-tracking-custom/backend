package com.aszaitsev.tasktrackerbackend.service;

import com.aszaitsev.tasktrackerbackend.service.dto.response.AuthResponse;
import com.aszaitsev.tasktrackerbackend.service.dto.request.LoginRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.request.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}