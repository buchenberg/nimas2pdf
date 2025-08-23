package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.entity.User;
import org.eightfoldconsulting.nimas2pdf.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom OAuth2 User Service that handles user registration and updates
 * from OAuth2 providers (Google, GitHub, etc.).
 */
@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("🔍 OAuth2UserService.loadUser() called!");
        logger.info("🔍 OAuth2UserService.loadUser() called!");
        
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        try {
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception ex) {
            logger.error("Error processing OAuth2 user", ex);
            System.err.println("❌ Error processing OAuth2 user: " + ex.getMessage());
            throw new OAuth2AuthenticationException("Error processing OAuth2 user: " + ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oauth2User.getAttributes());
        
        if (userInfo.getEmail() == null || userInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByProviderAndProviderId(provider, userInfo.getId())
                .orElse(null);
        
        if (user != null) {
            user = updateExistingUser(user, userInfo);
        } else {
            user = registerNewUser(userRequest, userInfo);
        }

        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        return new CustomOAuth2User(oauth2User.getAttributes(), nameAttributeKey, user);
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        
        // Check if user already exists with this email from another provider
        User existingUser = userRepository.findByEmail(userInfo.getEmail()).orElse(null);
        if (existingUser != null) {
            throw new OAuth2AuthenticationException(
                "User already exists with email " + userInfo.getEmail() + 
                " from provider " + existingUser.getProvider());
        }

        User user = new User();
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setProvider(provider);
        user.setProviderId(userInfo.getId());
        user.setPictureUrl(userInfo.getImageUrl());
        user.setStatus(User.UserStatus.ACTIVE);
        
        // First user becomes admin
        long userCount = userRepository.count();
        if (userCount == 0) {
            user.addRole(User.UserRole.ADMIN);
            logger.info("First user registered as admin: {}", user.getEmail());
        }

        user = userRepository.save(user);
        logger.info("New user registered: {} from provider {}", user.getEmail(), provider);
        
        return user;
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo userInfo) {
        boolean updated = false;
        
        if (!existingUser.getName().equals(userInfo.getName())) {
            existingUser.setName(userInfo.getName());
            updated = true;
        }
        
        if (!existingUser.getEmail().equals(userInfo.getEmail())) {
            existingUser.setEmail(userInfo.getEmail());
            updated = true;
        }
        
        if (userInfo.getImageUrl() != null && !userInfo.getImageUrl().equals(existingUser.getPictureUrl())) {
            existingUser.setPictureUrl(userInfo.getImageUrl());
            updated = true;
        }

        // Update last login time
        existingUser.updateLastLogin();
        updated = true;

        if (updated) {
            existingUser = userRepository.save(existingUser);
            logger.debug("Updated existing user: {}", existingUser.getEmail());
        }

        return existingUser;
    }
}
