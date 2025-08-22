package com.example.DrumkitTagger.service;

import com.example.DrumkitTagger.entity.User;
import com.example.DrumkitTagger.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setBanned(false);
        user.setDeleted(false);
        return userRepository.save(user);
    }

    public User register(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        return register(user); // wywołuje istniejącą metodę
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void softDelete(User user) {
        //userRepository.delete(user);
        user.setDeleted(true);
        user.setUsername("DELETED-USER-" + user.getId());
        user.setEmail("DELETED-USER-" + user.getId() + "@elitemcservers.com");
        user.setPassword(passwordEncoder.encode("DELETED-PASSWORD"));
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String email = null;

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            Object principal = oauthToken.getPrincipal();
            if (principal instanceof OidcUser oidcUser) {
                email = oidcUser.getEmail();
            } else {
                email = oauthToken.getPrincipal().getAttribute("email");
            }
        } else {
            email = authentication.getName();
        }

        if (email == null) return Optional.empty();
        return findByEmail(email);
    }
}
