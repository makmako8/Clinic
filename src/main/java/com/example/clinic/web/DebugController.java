package com.example.clinic.web;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//DebugController.java
@RestController
public class DebugController {
@GetMapping("/auth/whoami")
public Map<String,Object> whoami(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {
 return jwt == null ? Map.of("auth","none") : jwt.getClaims();
}
}
