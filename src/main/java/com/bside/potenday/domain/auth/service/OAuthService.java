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
    @Value("${oauth2.google.redirect-uri}")
    private String LOGIN_REDIRECT_URL;

    public User getGoogleAccessToken(String authCode) {
        System.out.println("OAuth 요청 시 사용되는 redirectUri: " + LOGIN_REDIRECT_URL);
        // 콘솔 출력 결과 : OAuth 요청 시 사용되는 redirectUri: http://localhost:3000/auth/google/callback
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret(GOOGLE_CLIENT_SECRET)
                .code(authCode)
                .redirectUri(LOGIN_REDIRECT_URL)
                .grantType("authorization_code").build();

        // 400 Bad Request: "{<EOL>  "error": "redirect_uri_mismatch",<EOL>
        ResponseEntity<GoogleResponse> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL,
                googleOAuthRequestParam, GoogleResponse.class);

        String idToken =  responseEntity.getBody().getId_token();  // id token (=jwt token, 디코딩 후 사용)

        Map<String, String> map = new HashMap<>();
        map.put("id_token", idToken);
        ResponseEntity<GoogleInfResponse> responseInfEntity = restTemplate.postForEntity(GOOGLE_TOKEN_INFO_URL,
                map, GoogleInfResponse.class);

        // 사용자 정보 저장 후
        // 닉네임 화면으로 return
        // 닉네임 정보는 후에 저장
        String providerId = responseInfEntity.getBody().getSub(); // Google의 고유 사용자 ID
        String email = responseInfEntity.getBody().getEmail();
        String username = responseInfEntity.getBody().getName();
        String profileImg = responseInfEntity.getBody().getPicture();

        Optional<User> optionalUser = userRepository.findByUserOauthProviderAndUserOauthProviderId(Provider.GOOGLE, providerId);

        boolean isNewUser;
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            isNewUser = false;
            // 기존 사용자라면 필요한 정보 갱신 가능
        } else {
            isNewUser = true;
            UserOauth userOauth = new UserOauth(Provider.GOOGLE, providerId, responseEntity.getBody().getAccess_token(), responseEntity.getBody().getRefresh_token());
            user = new User(username, email, profileImg, userOauth);
            userRepository.save(user);
        }
        user.setNewUser(isNewUser);

        return user;
    }

//    public ResponseEntity<GoogleResponse> processGoogleAccessToken(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String tokenInfoUrl = GOOGLE_TOKEN_INFO_URL + "?access_token=" + accessToken;
//        ResponseEntity<GoogleInfResponse> tokenInfoResponse = restTemplate.getForEntity(tokenInfoUrl, GoogleInfResponse.class);
//
//        if (!tokenInfoResponse.getStatusCode().is2xxSuccessful() || tokenInfoResponse.getBody() == null) {
//            throw new RuntimeException("토큰 검증에 실패했습니다.");
//        }
//
//        GoogleInfResponse tokenInfo = tokenInfoResponse.getBody();
//
//        String providerId = tokenInfo.getSub(); // Google의 고유 사용자 ID
//        String email = tokenInfo.getEmail();
//        String username = tokenInfo.getName();
//        String profileImg = tokenInfo.getPicture();
//
//        Optional<User> optionalUser = userRepository.findByUserOauthProviderAndUserOauthProviderId(Provider.GOOGLE, providerId);
//
//        User user;
//        if (optionalUser.isPresent()) {
//            user = optionalUser.get();
//        } else {
//            UserOauth userOauth = new UserOauth(Provider.GOOGLE, providerId, accessToken, null);
//            user = new User(username, email, profileImg, Job.WORKER, "칠리새우먹고싶다", userOauth);
//        }
//
//        userRepository.save(user);
//        return null;
//    }


}
