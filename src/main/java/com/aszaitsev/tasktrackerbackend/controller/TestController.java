package com.aszaitsev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Endpoints", description = "Тестовые эндпоинты для проверки авторизации")
public class TestController {

    @GetMapping("/public")
    @Operation(
            summary = "Публичный endpoint",
            description = "Доступен без авторизации"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос"
            )
    })
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    @GetMapping("/protected")
    @Operation(
            summary = "Защищенный endpoint",
            description = "Требует авторизацию (JWT токен)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован"
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public String protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "This is a protected endpoint. User: " + authentication.getName();
    }
}