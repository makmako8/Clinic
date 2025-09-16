package com.example.clinic.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.clinic.domain.Encounter;
import com.example.clinic.dto.EncounterResource;
import com.example.clinic.mapper.EncounterMapper;
import com.example.clinic.repo.EncounterRepository;
import com.example.clinic.repo.PatientRepository;
import com.example.clinic.security.SecurityUtils;

@Service
public class EncounterService {
    private final EncounterRepository encounterRepo;
    private final PatientRepository patientRepo;
    private final EncounterMapper mapper;
    
    public EncounterService(EncounterRepository encounterRepo, PatientRepository patientRepo, EncounterMapper mapper)
    {   
    	this.encounterRepo = encounterRepo;
    	this.patientRepo = patientRepo;
    	this.mapper = mapper;
    }
    @Transactional
    public EncounterResource create(Long patientId, String department, String reason) {
      Long tenantId = SecurityUtils.currentTenantIdRequired();

      if (patientId == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientId is required");
      }
      if (!patientRepo.existsByIdAndTenantId(patientId, tenantId)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found");
      }


      Encounter e = new Encounter();
      String doctorUserId = SecurityUtils.currentUsernameOrNull();
      if (doctorUserId != null) e.setDoctorUserId(doctorUserId);
      e.setTenantId(tenantId);
      e.setPatientId(patientId);
      e.setDepartment(department);
      e.setReason(reason);
      e.setVisitDateTime(LocalDateTime.now(ZoneOffset.UTC));



      return mapper.toResource(encounterRepo.save(e));
    }

    @Transactional(readOnly = true)
    public List<EncounterResource> listByPatient(Long patientId) {
      Long tenantId = SecurityUtils.currentTenantIdRequired();
      return encounterRepo
          .findByTenantIdAndPatientIdOrderByVisitDateTimeDesc(tenantId, patientId)
          .stream().map(mapper::toResource).toList();
    }
}
