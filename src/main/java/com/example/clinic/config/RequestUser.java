package com.example.clinic.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

@RequestScope
@Component
public class RequestUser {
public String username() { return getJwt().getClaimAsString("sub"); }
public Long tenantId() {
    var jwt = getJwt();
    Object v = jwt.getClaim("tenantId");         // 本命：キャメル
    if (v == null) v = jwt.getClaim("tenant_id"); // 移行用フォールバック（暫定）
    if (v == null) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "tenantId missing in JWT");
    }
    if (v instanceof Number n) return n.longValue();
    return Long.parseLong(v.toString());
}

public Long patientIdOrNull() { return getJwt().getClaim("patient_id"); }
public Set<String> roles() {
 List<String> rs = getJwt().getClaimAsStringList("roles");
 return rs == null ? Set.of() : rs.stream().collect(Collectors.toSet());
}
private Jwt getJwt() {
 Authentication a = SecurityContextHolder.getContext().getAuthentication();
 return (Jwt) a.getPrincipal();
}
public boolean hasRole(String role) { return roles().contains(role); }
}
