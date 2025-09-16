package com.example.clinic.service;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.example.clinic.config.RequestUser;

@Component
public class AccessChecker {
private final RequestUser ru;
public AccessChecker(RequestUser ru) { this.ru = ru; }

public void checkTenant(Long tenantId) {
 if (!ru.tenantId().equals(tenantId)) throw new AccessDeniedException("Wrong tenant");
}
public void checkPatientScope(Long resourcePatientId) {
 if (ru.hasRole("PATIENT")) {
   Long pid = ru.patientIdOrNull();
   if (pid == null || !pid.equals(resourcePatientId)) throw new AccessDeniedException("Patient scope");
 }
}
}
