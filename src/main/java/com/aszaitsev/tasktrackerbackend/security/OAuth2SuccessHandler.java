package com.aszaitsev.tasktrackerbackend.security;

import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.repository.UserRepository;
import com.aszaitsev.tasktrackerbackend.service.JwtService;
import com.aszaitsev.tasktrackerbackend.service.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);
    
    @Autowired
    private OAuth2Service oAuth2Service;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        
        HttpSession session = request.getSession(false);
        Boolean isLinkMode = session != null && Boolean.TRUE.equals(session.getAttribute("OAUTH_LINK_MODE"));
        String linkUsername = session != null ? (String) session.getAttribute("OAUTH_LINK_USER") : null;
        
        logger.info("OAuth2 callback - Session: {}, Link mode: {}", session != null, isLinkMode);
        
        try {
            String token;
            String targetUrl;
            
            if (isLinkMode && linkUsername != null) {
                // Режим привязки OAuth к существующему аккаунту
                User user = userRepository.findByUsername(linkUsername)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                
                oAuth2Service.linkOAuthAccount(user, oAuth2User, registrationId);
                
                // Очищаем сессию
                session.removeAttribute("OAUTH_LINK_MODE");
                session.removeAttribute("OAUTH_LINK_USER");
                
                token = jwtService.generateTokenFromUsername(user.getUsername());
                targetUrl = "/"; // Redirect to main after linking
            } else {
                // Обычный OAuth логин
                User user = oAuth2Service.processOAuthLogin(oAuth2User, registrationId);
                token = jwtService.generateTokenFromUsername(user.getUsername());
                
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    targetUrl = "/set-password.html"; // Redirect to set password page
                } else {
                    targetUrl = "/"; // Redirect to main
                }
            }
            
            String redirectUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("token", token)
                    .build()
                    .toUriString();
            
            logger.info("OAuth2 authentication successful, redirecting to: {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            
        } catch (Exception e) {
            logger.error("Error during OAuth2 authentication", e);
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            getRedirectStrategy().sendRedirect(request, response, "/oauth2/error?error=" + errorMessage);
        }
    }
}
