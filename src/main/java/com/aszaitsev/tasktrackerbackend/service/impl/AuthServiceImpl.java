package com.aszaitsev.tasktrackerbackend.service.impl;

import com.aszaitsev.tasktrackerbackend.model.GithubUser;
import com.aszaitsev.tasktrackerbackend.model.GoogleUser;
import com.aszaitsev.tasktrackerbackend.repository.GithubUserRepository;
import com.aszaitsev.tasktrackerbackend.repository.GoogleUserRepository;
import com.aszaitsev.tasktrackerbackend.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final GoogleUserRepository googleUserRepository;

    private final GithubUserRepository githubUserRepository;

    public AuthServiceImpl(GoogleUserRepository googleUserRepository, GithubUserRepository githubUserRepository) {
        this.googleUserRepository = googleUserRepository;
        this.githubUserRepository = githubUserRepository;
    }

    @Override
    public String authWithGoogle() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauthtoken) {
            OAuth2User user = oauthtoken.getPrincipal();
            System.out.println(user);
            if (user != null) {
                Map<String, Object> attrs = user.getAttributes();
                if (attrs.containsKey("sub")) {
                    Optional<GoogleUser> userOpt = googleUserRepository.findById((String) attrs.get("sub"));
                    if (userOpt.isEmpty()) {
                        GoogleUser googleUser = new GoogleUser();
                        googleUser.setId((String) attrs.get("sub"));
                        googleUser.setGiven_name((String) attrs.get("given_name"));
                        googleUser.setFamily_name((String) attrs.get("family_name"));
                        googleUser.setName((String) attrs.get("name"));
                        googleUser.setEmail((String) attrs.get("email"));
                        googleUser.setPicture((String) attrs.get("picture"));
                        googleUserRepository.save(googleUser);
                    }
                } else {
                    Optional<GithubUser> userOpt = githubUserRepository.findById((Integer) attrs.get("id"));
                    if(userOpt.isEmpty()) {
                        GithubUser githubUser = new GithubUser();
                        githubUser.setId((Integer) attrs.get("id"));
                        githubUser.setLogin((String) attrs.get("login"));
                        githubUser.setEmail((String) attrs.get("email"));
                        githubUser.setAvatar_url((String) attrs.get("avatar_url"));
                        githubUserRepository.save(githubUser);
                    }
                }
                return "Authenticated";
            }
        }
        throw new RuntimeException("No OAuth2AuthenticationToken found");
    }
}
