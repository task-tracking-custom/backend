package com.aszaitsev.tasktrackerbackend.service;

import com.aszaitsev.tasktrackerbackend.model.Role;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.repository.RoleRepository;
import com.aszaitsev.tasktrackerbackend.repository.UserOAuthLinkRepository;
import com.aszaitsev.tasktrackerbackend.repository.UserRepository;
import com.aszaitsev.tasktrackerbackend.service.dto.request.RegisterRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserOAuthLinkRepository userOAuthLinkRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("USER");
                    return roleRepository.save(role);
                });
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setHasPassword(user.getPassword() != null && !user.getPassword().isEmpty());
        response.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        response.setOauthLinks(userOAuthLinkRepository.findByUser(user).stream()
                .map(oauth -> new UserInfoResponse.OAuthLinkInfo(
                        oauth.getProvider(),
                        oauth.getEmail(),
                        oauth.getCreatedAt()
                ))
                .collect(Collectors.toList()));
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
    
    @Transactional
    public void setPasswordAndUsername(User user, String newUsername, String password) {
        if (!user.getUsername().equals(newUsername)) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already taken");
            }
            user.setUsername(newUsername);
        }
        
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
