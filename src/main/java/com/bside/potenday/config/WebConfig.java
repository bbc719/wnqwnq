package com.bside.potenday.config;

import com.bside.potenday.domain.auth.jwt.JwtAuthenticationFilter;
import com.bside.potenday.domain.auth.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS 설정을 위한 메서드
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 대해 CORS 설정을 추가
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 허용
                .allowedOrigins("https://jubjub.kr", "https://zubzub.vercel.app", "http://localhost:3000") // 허용할 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더를 허용
                .allowCredentials(true); // 자격 증명(쿠키, 인증 헤더 등)을 허용
    }

    private final JwtProvider jwtProvider;

    public WebConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public Filter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }
}
