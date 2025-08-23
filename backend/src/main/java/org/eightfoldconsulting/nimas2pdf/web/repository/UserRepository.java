package org.eightfoldconsulting.nimas2pdf.web.repository;

import org.eightfoldconsulting.nimas2pdf.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by OAuth provider and provider ID.
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    /**
     * Find all users with a specific role.
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") User.UserRole role);

    /**
     * Find all active users.
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * Find users who haven't logged in since a certain date.
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :since OR u.lastLoginAt IS NULL")
    List<User> findUsersNotLoggedInSince(@Param("since") LocalDateTime since);

    /**
     * Find users by provider.
     */
    List<User> findByProvider(String provider);

    /**
     * Check if user exists by email.
     */
    boolean existsByEmail(String email);

    /**
     * Check if user exists by provider and provider ID.
     */
    boolean existsByProviderAndProviderId(String provider, String providerId);

    /**
     * Count total active users.
     */
    long countByStatus(User.UserStatus status);

    /**
     * Find users created after a certain date.
     */
    List<User> findByCreatedAtAfter(LocalDateTime since);
}
