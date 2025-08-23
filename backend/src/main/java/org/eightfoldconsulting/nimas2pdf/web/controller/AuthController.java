package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.entity.User;
import org.eightfoldconsulting.nimas2pdf.web.service.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication-related operations.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AuthController {

    /**
     * Get current user information.
     */
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);

        if (principal instanceof CustomOAuth2User customUser) {
            User user = customUser.getUser();
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("pictureUrl", user.getPictureUrl());
            response.put("provider", user.getProvider());
            response.put("roles", user.getRoles());
            response.put("isAdmin", user.isAdmin());
            response.put("status", user.getStatus());
            response.put("lastLoginAt", user.getLastLoginAt());
        } else {
            // Fallback for basic OAuth2User
            response.put("name", principal.getName());
            response.put("email", principal.getAttribute("email"));
            response.put("pictureUrl", principal.getAttribute("picture"));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Check authentication status.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", principal != null);
        
        if (principal instanceof CustomOAuth2User customUser) {
            User user = customUser.getUser();
            response.put("isAdmin", user.isAdmin());
            response.put("roles", user.getRoles());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get available OAuth2 providers for login.
     */
    @GetMapping("/providers")
    public ResponseEntity<Map<String, Object>> getProviders() {
        Map<String, Object> response = new HashMap<>();
        
        Map<String, String> providers = new HashMap<>();
        providers.put("google", "/oauth2/authorization/google");
        providers.put("github", "/oauth2/authorization/github");
        providers.put("microsoft", "/oauth2/authorization/microsoft");
        
        response.put("providers", providers);
        return ResponseEntity.ok(response);
    }
}
