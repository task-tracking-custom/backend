package com.aszaitsev.tasktrackerbackend.service;

import com.aszaitsev.tasktrackerbackend.model.OAuthProvider;
import com.aszaitsev.tasktrackerbackend.model.Role;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.model.UserOAuthLink;
import com.aszaitsev.tasktrackerbackend.repository.RoleRepository;
import com.aszaitsev.tasktrackerbackend.repository.UserOAuthLinkRepository;
import com.aszaitsev.tasktrackerbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class OAuth2Service {
    
    private static final Logger logger = LoggerFactory.getLogger(OAuth2Service.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserOAuthLinkRepository oauthLinkRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Transactional
    public User processOAuthLogin(OAuth2User oAuth2User, String registrationId) {
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());
        String providerId = getProviderId(oAuth2User, provider);
        String email = getEmail(oAuth2User, provider);
        
        logger.info("Processing OAuth login for provider: {}, providerId: {}", provider, providerId);
        
        // Проверяем существует ли уже OAuth привязка
        UserOAuthLink existingLink = oauthLinkRepository
                .findByProviderAndProviderId(provider, providerId)
                .orElse(null);
        
        if (existingLink != null) {
            logger.info("Found existing OAuth link for user: {}", existingLink.getUser().getUsername());
            return existingLink.getUser();
        }
        
        // Создаем нового пользователя
        logger.info("Creating new user via OAuth");
        String username = generateUsername(oAuth2User, provider, providerId);
        
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("USER");
                    return roleRepository.save(role);
                });
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(null);
        newUser.setEnabled(true);
        newUser.setRoles(roles);
        
        newUser = userRepository.save(newUser);
        
        // Создаем OAuth привязку
        UserOAuthLink newLink = new UserOAuthLink();
        newLink.setUser(newUser);
        newLink.setProvider(provider);
        newLink.setProviderId(providerId);
        newLink.setEmail(email);
        
        oauthLinkRepository.save(newLink);
        
        logger.info("Created new user {} with OAuth link", username);
        return newUser;
    }
    
    @Transactional
    public void linkOAuthAccount(User user, OAuth2User oAuth2User, String registrationId) {
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());
        String providerId = getProviderId(oAuth2User, provider);
        String email = getEmail(oAuth2User, provider);
        
        logger.info("Linking OAuth account for user: {}, provider: {}", user.getUsername(), provider);
        
        // Проверяем не привязан ли уже этот OAuth аккаунт к другому пользователю
        if (oauthLinkRepository.existsByProviderAndProviderId(provider, providerId)) {
            throw new RuntimeException("This OAuth account is already linked to another user");
        }
        
        // Проверяем не привязан ли уже этот провайдер к текущему пользователю
        if (oauthLinkRepository.existsByUserAndProvider(user, provider)) {
            throw new RuntimeException("You already have a " + provider + " account linked");
        }
        
        // Создаем новую привязку
        UserOAuthLink newLink = new UserOAuthLink();
        newLink.setUser(user);
        newLink.setProvider(provider);
        newLink.setProviderId(providerId);
        newLink.setEmail(email);
        
        oauthLinkRepository.save(newLink);
        
        logger.info("Successfully linked {} account for user {}", provider, user.getUsername());
    }
    
    private String getProviderId(OAuth2User oAuth2User, OAuthProvider provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return switch (provider) {
            case GITHUB -> String.valueOf(attributes.get("id"));
            case GOOGLE -> (String) attributes.get("sub");
        };
    }
    
    private String getEmail(OAuth2User oAuth2User, OAuthProvider provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        
        if (email == null || email.isEmpty()) {
            String providerId = getProviderId(oAuth2User, provider);
            email = providerId + "@" + provider.name().toLowerCase() + ".oauth.temp";
        }
        
        return email;
    }
    
    private String generateUsername(OAuth2User oAuth2User, OAuthProvider provider, String providerId) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String username;
        
        if (provider == OAuthProvider.GITHUB) {
            username = (String) attributes.get("login");
        } else {
            username = (String) attributes.get("email");
            if (username != null && username.contains("@")) {
                username = username.split("@")[0];
            }
        }
        
        if (username == null || username.isEmpty()) {
            username = provider.name().toLowerCase() + "_" + providerId;
        }
        
        // Проверяем уникальность
        String finalUsername = username;
        int counter = 1;
        while (userRepository.existsByUsername(finalUsername)) {
            finalUsername = username + counter;
            counter++;
        }
        
        return finalUsername;
    }
}
