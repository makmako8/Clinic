package com.example.clinic.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.clinic.config.RequestUser;
import com.example.clinic.domain.Patient;
import com.example.clinic.dto.PatientResource;
import com.example.clinic.mapper.PatientMapper;
import com.example.clinic.repo.PatientRepository;
import com.example.clinic.security.SecurityUtils;

@Service @Transactional
public class PatientService {
private final PatientRepository repo;


private final PatientMapper mapper;
private final RequestUser ru;
private final AccessChecker ac;

public PatientService(PatientRepository r, PatientMapper m, RequestUser ru, AccessChecker ac){
 this.repo=r; this.mapper=m; this.ru=ru; this.ac=ac;
}

@Audited(action="READ", resource="Patient")
@Transactional(readOnly = true)
public List<PatientResource> search(String q) {
    Long tenantId = SecurityUtils.currentTenantIdRequired();
    String keyword = (q == null) ? "" : q.trim();
    return repo.search(tenantId, keyword).stream()
               .map(mapper::toResource)
               .toList();
}
@Transactional
public PatientResource create(String identifier, String name, String gender) {
	Long tenantId = SecurityUtils.currentTenantIdRequired();
	Patient p = new Patient();
    p.setTenantId(tenantId);
    p.setMrn(identifier);   // ★ identifier → mrn に詰め替え
    p.setName(name);
    repo.save(p);
    return mapper.toResource(p);
}
Patient getEntityOr404(Long id) {
    Long tenantId = SecurityUtils.currentTenantIdRequired();
    return repo.findByIdAndTenantId(id, tenantId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
}
// String 版
private String normalizeGenderString(String g) {
  if (g == null) return null;
  g = g.trim().toLowerCase();
  return switch (g) {
    case "male", "m", "man", "男", "男性" -> "male";
    case "female", "f", "woman", "女", "女性" -> "female";
    case "other" -> "other";
    case "unknown", "unk", "" -> "unknown";
    default -> g; // 想定外はそのまま
  };
}
@Transactional(readOnly = true)
public PatientResource get(Long id) {
    Long tenantId = SecurityUtils.currentTenantIdRequired();
    Patient p = repo.findById(id)
            .filter(pp -> tenantId.equals(pp.getTenantId()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    return mapper.toResource(p);
}

/* Enum 版（エンティティが Enum のときだけ有効化）
private Sex mapToSexEnum(String g) {
  if (g == null) return Sex.UNKNOWN;
  return switch (g.trim().toLowerCase()) {
    case "male", "m", "man", "男", "男性" -> Sex.MALE;
    case "female", "f", "woman", "女", "女性" -> Sex.FEMALE;
    case "other" -> Sex.OTHER;
    default -> Sex.UNKNOWN;
  };
}
*/
}
