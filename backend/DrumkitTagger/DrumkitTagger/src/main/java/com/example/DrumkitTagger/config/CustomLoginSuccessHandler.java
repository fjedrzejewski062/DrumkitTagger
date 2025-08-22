package com.example.DrumkitTagger.config;

import com.example.DrumkitTagger.service.UserService;
import com.example.DrumkitTagger.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public CustomLoginSuccessHandler(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // aktualizacja lastLogin
        userService.getAuthenticatedUser(authentication).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userService.update(user);
        });

        // przygotuj JSON z userem (i ewentualnym tokenem)
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Login successful");
        body.put("username", authentication.getName());

        // jeżeli masz token JWT – tu możesz go wygenerować i dodać
        // String token = jwtService.generateToken(authentication);
        // body.put("token", token);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), body);
    }
}

