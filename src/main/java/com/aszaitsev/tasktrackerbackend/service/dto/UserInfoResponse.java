package com.aszaitsev.tasktrackerbackend.service.dto;

import com.aszaitsev.tasktrackerbackend.model.OAuthProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserInfoResponse {
    
    private Long id;
    private String username;
    private String email;
    private Boolean hasPassword;
    private Set<String> roles = new HashSet<>();
    private List<OAuthLinkInfo> oauthLinks = new ArrayList<>();
    private LocalDateTime createdAt;
    
    // Конструкторы
    public UserInfoResponse() {}
    
    public UserInfoResponse(Long id, String username, String email, Boolean hasPassword, 
                           Set<String> roles, List<OAuthLinkInfo> oauthLinks, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.hasPassword = hasPassword;
        this.roles = roles != null ? roles : new HashSet<>();
        this.oauthLinks = oauthLinks != null ? oauthLinks : new ArrayList<>();
        this.createdAt = createdAt;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Boolean getHasPassword() {
        return hasPassword;
    }
    
    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public List<OAuthLinkInfo> getOauthLinks() {
        return oauthLinks;
    }
    
    public void setOauthLinks(List<OAuthLinkInfo> oauthLinks) {
        this.oauthLinks = oauthLinks;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Вложенный класс OAuthLinkInfo
    public static class OAuthLinkInfo {
        
        private OAuthProvider provider;
        private String email;
        private LocalDateTime linkedAt;
        
        // Конструкторы
        public OAuthLinkInfo() {}
        
        public OAuthLinkInfo(OAuthProvider provider, String email, LocalDateTime linkedAt) {
            this.provider = provider;
            this.email = email;
            this.linkedAt = linkedAt;
        }
        
        // Геттеры и сеттеры
        public OAuthProvider getProvider() {
            return provider;
        }
        
        public void setProvider(OAuthProvider provider) {
            this.provider = provider;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public LocalDateTime getLinkedAt() {
            return linkedAt;
        }
        
        public void setLinkedAt(LocalDateTime linkedAt) {
            this.linkedAt = linkedAt;
        }
    }
}