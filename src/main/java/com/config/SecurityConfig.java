package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String s3BucketUrl;

    public SecurityConfig(@Value("${aws.s3.bucket-url:}") String s3BucketUrl) {
        this.s3BucketUrl = s3BucketUrl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String s3UrlForCsp = (s3BucketUrl != null && !s3BucketUrl.isBlank()) ? " " + s3BucketUrl : "";

        String cspDirectives =
                "default-src 'self'; " +
                        "frame-src 'self' https://www.youtube.com" + s3UrlForCsp + "; " +
                        "media-src 'self'" + s3UrlForCsp + "; " +
                        "img-src 'self' data: https://placehold.co" + s3UrlForCsp + "; " +
                        "script-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "font-src 'self';";

        http
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(cspDirectives)
                        )
                );

        return http.build();
    }
}