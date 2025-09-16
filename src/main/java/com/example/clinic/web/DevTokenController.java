package com.example.clinic.web;



import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DevTokenController {

private final JwtEncoder jwtEncoder;

@Value("${app.jwt.issuer:clinic-mvp}") private String issuer;
@Value("${app.jwt.ttl-seconds}") long ttlSeconds;

@PostMapping(path = "/auth/dev-token", consumes = MediaType.APPLICATION_JSON_VALUE)
public Map<String, Object> token(@RequestBody DevTokenRequest req) {
 // バリデーション（開発用の簡易チェック）
 if (req.getUsername() == null || req.getTenantId() == null) {
   throw new IllegalArgumentException("username and tenantId are required");
 }

 Instant now = Instant.now();
 JwtClaimsSet claims = JwtClaimsSet.builder()
     .issuer(issuer)
     .issuedAt(now)
     .expiresAt(now.plusSeconds(ttlSeconds))
     .subject(req.getUsername())
     // ★ ここが重要：クレームに必ず入れる
     .claim("tenantId", req.getTenantId())
     .claim("roles", req.getRoles() == null ? List.of() : req.getRoles())
     .claim("userId", 1)
     .build();

 JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
 String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

 return Map.of("access_token", token, "token_type", "Bearer", "expires_in", ttlSeconds);
}

@Data
public static class DevTokenRequest {
 private String username;
 private Long tenantId;
 private List<String> roles;
}
}

