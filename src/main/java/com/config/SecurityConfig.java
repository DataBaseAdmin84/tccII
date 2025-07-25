package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final String s3BucketUrl = "https://" + s3BucketName + ".s3.us-east-2.amazonaws.com";

        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self';" +
                                                "script-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com https://cdn.jsdelivr.net;" +
                                                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdn.jsdelivr.net https://cdnjs.cloudflare.com;" +
                                                "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com;" +
                                                "img-src 'self' data: " + s3BucketUrl + " https://placehold.co;" +
                                                "media-src 'self' " + s3BucketUrl + ";" +
                                                "frame-src 'self' " + s3BucketUrl + " https://www.youtube.com"
                                )
                        )
                );

        return http.build();
    }

    private String buildCspPolicy(String s3BucketUrl, String... scriptHashes) {
        String scriptSrcHashes = String.join(" ", scriptHashes);
        return String.join("; ",
                "default-src 'self'",
                "script-src 'self' " + scriptSrcHashes + " https://cdn.tailwindcss.com https://cdn.jsdelivr.net",
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdn.jsdelivr.net https://cdnjs.cloudflare.com",
                "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com",
                "img-src 'self' data: " + s3BucketUrl + " https://placehold.co",
                "media-src 'self' " + s3BucketUrl,
                "frame-src 'self' " + s3BucketUrl + " https://www.youtube.com"
        );
    }
}