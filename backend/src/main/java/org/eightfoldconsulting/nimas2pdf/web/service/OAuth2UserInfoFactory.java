package org.eightfoldconsulting.nimas2pdf.web.service;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

/**
 * Factory class to create OAuth2UserInfo objects based on the provider.
 */
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch (registrationId.toLowerCase()) {
            case "google":
                return new GoogleOAuth2UserInfo(attributes);
            case "github":
                return new GitHubOAuth2UserInfo(attributes);
            case "microsoft":
            case "azure":
                return new MicrosoftOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationException("Login with " + registrationId + " is not supported");
        }
    }

    /**
     * Google OAuth2 user information implementation.
     */
    public static class GoogleOAuth2UserInfo extends OAuth2UserInfo {
        public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
            super(attributes);
        }

        @Override
        public String getId() {
            return (String) attributes.get("sub");
        }

        @Override
        public String getName() {
            return (String) attributes.get("name");
        }

        @Override
        public String getEmail() {
            return (String) attributes.get("email");
        }

        @Override
        public String getImageUrl() {
            return (String) attributes.get("picture");
        }
    }

    /**
     * GitHub OAuth2 user information implementation.
     */
    public static class GitHubOAuth2UserInfo extends OAuth2UserInfo {
        public GitHubOAuth2UserInfo(Map<String, Object> attributes) {
            super(attributes);
        }

        @Override
        public String getId() {
            return String.valueOf(attributes.get("id"));
        }

        @Override
        public String getName() {
            String name = (String) attributes.get("name");
            return name != null ? name : (String) attributes.get("login");
        }

        @Override
        public String getEmail() {
            return (String) attributes.get("email");
        }

        @Override
        public String getImageUrl() {
            return (String) attributes.get("avatar_url");
        }
    }

    /**
     * Microsoft/Azure OAuth2 user information implementation.
     */
    public static class MicrosoftOAuth2UserInfo extends OAuth2UserInfo {
        public MicrosoftOAuth2UserInfo(Map<String, Object> attributes) {
            super(attributes);
        }

        @Override
        public String getId() {
            return (String) attributes.get("id");
        }

        @Override
        public String getName() {
            return (String) attributes.get("displayName");
        }

        @Override
        public String getEmail() {
            return (String) attributes.get("mail");
        }

        @Override
        public String getImageUrl() {
            // Microsoft Graph doesn't provide image URL directly
            return null;
        }
    }
}
