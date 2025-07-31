package com.aszaitsev.tasktrackerbackend.controller;

import com.aszaitsev.tasktrackerbackend.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/google")
    public RedirectView authWithGoogle() {
        authService.authWithGoogle();
        return new RedirectView("http://localhost:3000/auth");
    }
}
