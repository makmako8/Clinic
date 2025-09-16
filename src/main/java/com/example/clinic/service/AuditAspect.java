package com.example.clinic.service;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.example.clinic.config.RequestUser;
import com.example.clinic.domain.AuditLog;
import com.example.clinic.repo.AuditLogRepository;

@Aspect @Component
public class AuditAspect {
private final AuditLogRepository repo; private final RequestUser ru; private final HttpServletRequest req;
public AuditAspect(AuditLogRepository r, RequestUser ru, HttpServletRequest req){ this.repo=r; this.ru=ru; this.req=req; }

@Around("@annotation(com.example.clinic.service.Audited)")
public Object around(ProceedingJoinPoint pjp) throws Throwable {
 Object ret = pjp.proceed();
 Audited an = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(Audited.class);
 // resourceId は戻り値や引数から引ける場合のみ簡易で保存（MVP：retがEntity/DTOならid拾う）
 Long rid = extractId(ret);
 AuditLog log = new AuditLog();
 log.setTenantId(ru.tenantId());
 log.setActorUserId(ru.username());
 log.setAction(an.action());
 log.setResourceType(an.resource());
 log.setResourceId(rid == null ? -1L : rid);
 log.setIp(req.getRemoteAddr());
 repo.save(log);
 return ret;
}
private Long extractId(Object o){
 try {
   if (o == null) return null;
   var m = o.getClass().getMethod("id");
   Object v = m.invoke(o);
   return v instanceof Long ? (Long)v : null;
 } catch (Exception e) { return null; }
}
}
