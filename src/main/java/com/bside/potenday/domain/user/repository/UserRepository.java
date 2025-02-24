package com.bside.potenday.domain.user.repository;

import com.bside.potenday.domain.auth.domain.Provider;
import com.bside.potenday.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserOauthProviderAndUserOauthProviderId(Provider provider, String providerId);
}
