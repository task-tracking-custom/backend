package com.aszaitsev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth2")
@Tag(name = "OAuth 2.0", description = "API для OAuth 2.0 аутентификации через GitHub и Google")
public class OAuth2Controller {
    
    @GetMapping("/link/{provider}")
    @Operation(
            summary = "Редирект на OAuth провайдер для привязки",
            description = "Перенаправляет на страницу авторизации OAuth провайдера (GitHub или Google) для привязки аккаунта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Редирект на OAuth провайдер"
            )
    })
    public RedirectView linkOAuthProvider(
            @Parameter(description = "OAuth провайдер (github или google)", example = "github")
            @PathVariable String provider) {
        return new RedirectView("/oauth2/authorization/" + provider);
    }
}