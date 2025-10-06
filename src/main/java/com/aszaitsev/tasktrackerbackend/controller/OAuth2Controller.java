package com.aszaitsev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth2")
@Tag(name = "OAuth 2.0", description = "API для OAuth 2.0 аутентификации через GitHub и Google")
public class OAuth2Controller {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @GetMapping("/link/{provider}")
    @Operation(
            summary = "Редирект на OAuth провайдер для регистрации или привязки",
            description = "Перенаправляет на страницу авторизации OAuth провайдера (GitHub или Google). " +
                    "Если пользователь авторизован (передан JWT токен), OAuth аккаунт будет привязан к существующему пользователю. " +
                    "Если не авторизован - будет выполнена регистрация/вход через OAuth."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Редирект на OAuth провайдер"
            )
    })
    public RedirectView linkOAuthProvider(
            @Parameter(description = "OAuth провайдер (github или google)", example = "github")
            @PathVariable String provider,
            HttpSession session) {
        
        // Получаем текущего авторизованного пользователя (если есть)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Проверяем, авторизован ли пользователь через JWT (UsernamePasswordAuthenticationToken)
        // OAuth2AuthenticationToken НЕ считается, так как это временная аутентификация в процессе OAuth
        boolean isAuthenticatedWithJwt = authentication != null && 
                                         authentication instanceof UsernamePasswordAuthenticationToken &&
                                         authentication.isAuthenticated() && 
                                         authentication.getName() != null &&
                                         !authentication.getName().equals("anonymousUser");
        
        if (isAuthenticatedWithJwt && authentication != null) {
            // Режим привязки OAuth к существующему аккаунту
            String username = authentication.getName();
            logger.info("User {} (authenticated with JWT) is linking OAuth provider: {}", username, provider);
            
            session.setAttribute("OAUTH_LINK_MODE", true);
            session.setAttribute("OAUTH_LINK_USER", username);
            
            logger.info("Session attributes set - Link mode: true, User: {}", username);
        } else {
            // Обычная регистрация/вход через OAuth
            logger.info("Anonymous user is registering/logging in via OAuth provider: {}", provider);
            
            // Очищаем атрибуты сессии от предыдущих попыток
            session.removeAttribute("OAUTH_LINK_MODE");
            session.removeAttribute("OAUTH_LINK_USER");
            logger.info("Cleared session attributes for anonymous OAuth registration");
        }
        
        return new RedirectView("/oauth2/authorization/" + provider);
    }
}