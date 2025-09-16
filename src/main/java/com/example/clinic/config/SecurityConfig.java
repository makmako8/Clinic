package com.example.clinic.config;

import static java.nio.charset.StandardCharsets.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

@Value("${app.jwt.secret}") 
private String secret;

@Bean
JwtDecoder jwtDecoder() {
    SecretKey key = new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
}
@Bean
JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes(UTF_8)));
}
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
  // これで /auth/** はセキュリティフィルタの “外” になります（= 401 が出なくなる）
  return web -> web.ignoring().requestMatchers("/auth/**");
}


/** 1) /auth/** は完全開放（JWTフィルタを適用しないチェーン） */
@Bean
@Order(1)
SecurityFilterChain authChain(HttpSecurity http) throws Exception {
 http.securityMatcher(new AntPathRequestMatcher("/auth/**"))
     .csrf(csrf -> csrf.disable())
     .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
 // ここでは oauth2ResourceServer(...) を絶対に設定しない
 return http.build();
}

/** 2) 残りすべてに JWT + RBAC を適用するチェーン */

@Bean
@Order(2)
SecurityFilterChain apiChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthConverter) throws Exception {
    http
        .securityMatcher(new AntPathRequestMatcher("/**"))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            // POST /api/patients は「認証のみ」なら authenticated()、ロールで縛るなら hasRole("DOCTOR")
            .requestMatchers(HttpMethod.POST, "/api/patients").authenticated()
            .requestMatchers("/error", "/error/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(HttpMethod.GET,  "/api/patients/**").hasAnyRole("DOCTOR","NURSE","ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/patients/**").hasAnyRole("DOCTOR","NURSE","ADMIN")
            .requestMatchers(HttpMethod.GET,  "/api/patients/*/encounters").hasAnyRole("DOCTOR","NURSE","ADMIN","PATIENT")
            .requestMatchers(HttpMethod.POST, "/api/encounters/**").hasAnyRole("DOCTOR","NURSE")
            .requestMatchers(HttpMethod.POST, "/api/encounters/*/notes").hasAnyRole("DOCTOR","NURSE")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth -> oauth
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
        );

    return http.build();
}
@Bean
SecurityFilterChain security(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/patients").hasRole("DOCTOR")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth -> oauth
            .jwt(jwt -> jwt
                .decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        );
    return http.build();
}
@Bean
JwtAuthenticationConverter jwtAuthenticationConverter() {
    var conv = new JwtAuthenticationConverter();
    conv.setJwtGrantedAuthoritiesConverter(jwt -> {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) return Collections.emptyList();
        List<GrantedAuthority> list = new ArrayList<>();
        for (String r : roles) {
            list.add(new SimpleGrantedAuthority("ROLE_" + r));
        }
        return list; // Collection<GrantedAuthority> として返る
    });
    return conv;
}
}

