package com.bside.potenday.domain.auth.service;

import com.bside.potenday.domain.auth.domain.GoogleInfResponse;
import com.bside.potenday.domain.auth.domain.GoogleRequest;
import com.bside.potenday.domain.auth.domain.GoogleResponse;
import com.bside.potenday.domain.auth.domain.Provider;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.domain.UserOauth;
import com.bside.potenday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    @Autowired
    private UserRepository userRepository;

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo"; // 토큰에 포함된 사용자의 이메일, 사용자 고유 ID(sub), 토큰 발행 시간, 만료 시간 등의 정보를 JSON 형태로 반환
    @Value("${oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    public User getGoogleAccessToken(String authCode) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret(GOOGLE_CLIENT_SECRET)
                .code(authCode)
                .redirectUri("postmessage")
                .grantType("authorization_code").build();

        ResponseEntity<GoogleResponse> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL,
                googleOAuthRequestParam, GoogleResponse.class);

        String idToken =  responseEntity.getBody().getId_token();

        Map<String, String> params = new HashMap<>();
        params.put("id_token", idToken);

        ResponseEntity<GoogleInfResponse> responseInfEntity = restTemplate.postForEntity(
                GOOGLE_TOKEN_INFO_URL, params, GoogleInfResponse.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Google OAuth 토큰 검증 실패");
        }

        String providerId = responseInfEntity.getBody().getSub(); // Google의 고유 사용자 ID
        String email = responseInfEntity.getBody().getEmail();
        String username = responseInfEntity.getBody().getName();
        String profileImg = responseInfEntity.getBody().getPicture();

        Optional<User> optionalUser = userRepository.findByUserOauthProviderAndUserOauthProviderId(Provider.GOOGLE, providerId);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            UserOauth userOauth = new UserOauth(Provider.GOOGLE, providerId, responseEntity.getBody().getAccess_token(), responseEntity.getBody().getRefresh_token());
            user = new User(username, email, profileImg, userOauth);
            userRepository.save(user);
        }

        return user;
    }
}
