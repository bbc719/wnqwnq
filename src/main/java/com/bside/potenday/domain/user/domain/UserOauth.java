package com.bside.potenday.domain.user.domain;

import com.bside.potenday.domain.auth.domain.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Embeddable
@NoArgsConstructor
public class UserOauth {
    // OAuth 제공자
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;
    // OAuth 제공자로부터 받은 고유 사용자 ID
    @Column(name = "provider_id", nullable = false)
    private String providerId;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "refresh_token")
    private String refreshToken;

    public UserOauth(Provider provider, String providerId, String accessToken, String refreshToken) {
        this.provider = provider;
        this.providerId = providerId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
