package com.example.clinic.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Long currentTenantIdRequired() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jat) {
          Object v = jat.getToken().getClaim("tenantId");
          if (v instanceof Integer i) return i.longValue();
          if (v instanceof Long l) return l;
          if (v instanceof String s) return Long.parseLong(s);
        }
        throw new IllegalStateException("tenantId is missing in JWT");
      }


    /** tenantId を JWT から取得。無ければ null */
    public static Long currentTenantIdOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jat) {
            return readTenantIdFrom(jat.getToken());
        }
        return null;
    }

    /** camelCase と snake_case のどちらのクレームでも読めるようにする */
    public static Long readTenantIdFrom(Jwt jwt) {
        Object v = jwt.getClaim("tenantId");
        if (v == null) v = jwt.getClaim("tenant_id");
        if (v == null) return null;

        if (v instanceof Number n) return n.longValue();
        try {
            return Long.parseLong(String.valueOf(v));
        } catch (NumberFormatException ignore) {
            return null;
        }
    }
    /** ユーザー名（JWT の sub または preferred_username） */
    public static String currentUsernameOrNull() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a instanceof JwtAuthenticationToken jat) {
            String sub = jat.getToken().getSubject(); // "sub"
            if (sub != null && !sub.isBlank()) return sub;
            Object v = jat.getToken().getClaim("preferred_username");
            if (v != null) return String.valueOf(v);
        }
        return null;
    }
    
    // ★ 追加：数値の userId を返す（JWT の "userId" を想定、なければ null）
    public static Long currentNumericUserIdOrNull() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth instanceof JwtAuthenticationToken jat) {
        Object v = jat.getToken().getClaim("userId"); // Devトークンに入れておくと便利
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof Long l) return l;
        if (v instanceof String s) {
          try { return Long.parseLong(s); } catch (NumberFormatException ignore) {}
        }
      }
      return null;
    }


}
