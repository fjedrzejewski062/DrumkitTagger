package com.example.DrumkitTagger.controller;

import com.example.DrumkitTagger.entity.User;
import com.example.DrumkitTagger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Rejestracja użytkownika */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user,
                                          @RequestParam("confirmPassword") String confirmPassword) {
        try {
            User createdUser = userService.register(user, confirmPassword);
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "user", createdUser
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    /** Pobranie danych zalogowanego użytkownika */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {
        Optional<User> userOpt = userService.getAuthenticatedUser(authentication);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        User user = userOpt.get();
        return ResponseEntity.ok(Map.of(
                "message", "User fetched successfully",
                "user", user
        ));
    }

    /** Soft delete konta użytkownika */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        userService.getAuthenticatedUser(authentication)
                .ifPresent(userService::softDelete);
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }

    /** Aktualizacja profilu (opcjonalnie) */
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody User updatedUser) {
        Optional<User> userOpt = userService.getAuthenticatedUser(authentication);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        User user = userOpt.get();
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        // w razie potrzeby można dodać aktualizację innych pól
        userService.update(user);

        return ResponseEntity.ok(Map.of("message", "Profile updated successfully", "user", user));
    }
}
