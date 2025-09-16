package com.example.clinic.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.clinic.config.RequestUser;
import com.example.clinic.domain.Encounter;
import com.example.clinic.domain.Note;
import com.example.clinic.dto.NoteResource;
import com.example.clinic.mapper.NoteMapper;
import com.example.clinic.repo.EncounterRepository;
import com.example.clinic.repo.NoteRepository;
import com.example.clinic.security.SecurityUtils;

@Service @Transactional
public class NoteService {
private final NoteRepository repo;
private final NoteMapper mapper;
private final EncounterRepository encRepo;
private final RequestUser ru;
private final AccessChecker ac;
private final EncounterService encounters;
private final NoteRepository noteRepo;
public NoteService(NoteRepository r, 
		NoteMapper m, RequestUser ru,
		AccessChecker ac,
		EncounterService e, NoteRepository noteRepo, EncounterRepository encRepo){
 this.repo=r;
 this.mapper=m;
 this.ru=ru;
 this.ac=ac;
 this.encounters=e;
 this.noteRepo=noteRepo;
 this.encRepo = encRepo;
}

@Audited(action="CREATE", resource="Note")
@Transactional
public NoteResource addNote(Long encounterId, String text){
	Long tenantId = SecurityUtils.currentTenantIdRequired();
	Encounter enc = encRepo.findById(encounterId)
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encounter not found"));
    if (encounterId == null || text == null || text.isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "encounterId and text are required");
      }


    // 同一テナント内で Encounter が存在するかチェック（情報漏えい防止のため404）
    if (!encRepo.existsByIdAndTenantId(encounterId, tenantId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encounter not found");
    }
    if (!tenantId.equals(enc.getTenantId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong tenant");
    }

    Note n = new Note();
    n.setTenantId(tenantId);
    n.setEncounterId(encounterId);   // ★ setEncounter(...) ではなく setEncounterId(...)
    n.setText(text);
    String author = SecurityUtils.currentUsernameOrNull(); // "dr_sato" など
    if (author != null) {
        n.setAuthorUserId(author); // フィールド型が String の場合
    }
    n.setText(text);

    return mapper.toResource(noteRepo.save(n));
}
public Note getEntityOr404(Long id) {
    return noteRepo.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
  }
}
