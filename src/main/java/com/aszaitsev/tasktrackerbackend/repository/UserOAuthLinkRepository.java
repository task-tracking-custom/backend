package com.aszaitsev.tasktrackerbackend.repository;

import com.aszaitsev.tasktrackerbackend.model.OAuthProvider;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.model.UserOAuthLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOAuthLinkRepository extends JpaRepository<UserOAuthLink, Long> {
    
    @Query("SELECT link FROM UserOAuthLink link JOIN FETCH link.user u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.oauthLinks WHERE link.provider = :provider AND link.providerId = :providerId")
    Optional<UserOAuthLink> findByProviderAndProviderId(@Param("provider") OAuthProvider provider, @Param("providerId") String providerId);
    
    Optional<UserOAuthLink> findByUserAndProvider(User user, OAuthProvider provider);
    
    List<UserOAuthLink> findByUser(User user);
    
    boolean existsByProviderAndProviderId(OAuthProvider provider, String providerId);
    
    boolean existsByUserAndProvider(User user, OAuthProvider provider);
    
    void deleteByUserAndProvider(User user, OAuthProvider provider);
}
