package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom implementation of OAuth2User that includes our User entity.
 */
public class CustomOAuth2User implements OAuth2User {
    
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final User user;

    public CustomOAuth2User(Map<String, Object> attributes, String nameAttributeKey, User user) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        if (nameAttributeKey != null) {
            return String.valueOf(attributes.get(nameAttributeKey));
        }
        return user.getName();
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public boolean hasRole(User.UserRole role) {
        return user.hasRole(role);
    }

    public boolean isAdmin() {
        return user.isAdmin();
    }
}
