package com.example.clinic.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic.domain.Encounter;
public interface EncounterRepository extends JpaRepository<Encounter, Long> {
List<Encounter> findByTenantIdAndPatientIdOrderByVisitDateTimeDesc(Long tenantId, Long patientId);
boolean existsByIdAndTenantId(Long id, Long tenantId);
}
