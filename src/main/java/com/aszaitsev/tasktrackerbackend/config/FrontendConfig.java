package com.aszaitsev.tasktrackerbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "frontend")
public class FrontendConfig {
    
    private boolean enabled = false;
    private String url = "http://localhost:3000";
    private String oauthCallback = "/auth/callback";
    
    // Геттеры и сеттеры
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getOauthCallback() {
        return oauthCallback;
    }
    
    public void setOauthCallback(String oauthCallback) {
        this.oauthCallback = oauthCallback;
    }
    
    public String getOAuthCallbackUrl() {
        return url + oauthCallback;
    }
}