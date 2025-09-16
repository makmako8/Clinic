package com.example.clinic.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic.domain.AuditLog;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> { }
