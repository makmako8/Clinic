package com.example.clinic.web;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinic.dto.EncounterResource;
import com.example.clinic.dto.NoteResource;
import com.example.clinic.service.EncounterService;
import com.example.clinic.service.NoteService;
@RestController
@RequestMapping("/api/encounters")
public class EncounterController {
private final EncounterService es;
private final NoteService ns;
public EncounterController(EncounterService es, NoteService ns){ 
	this.es=es; this.ns=ns;
	}

// GET /api/patients/{id}/encounters
@GetMapping("/api/patients/{id}/encounters")
public List<EncounterResource> list(@PathVariable Long id){
 return es.listByPatient(id);
}

// POST /api/encounters  { patientId, department, reason }
record NewEncounter(Long patientId, @NotBlank String department, String reason) {}

@PostMapping("/api/encounters")
public EncounterResource create(@RequestBody NewEncounter req){
 return es.create(req.patientId(), req.department(), req.reason());
}

// POST /api/encounters/{id}/notes  { text }
public static record NewNote(@NotBlank String text) {}


@PostMapping("/{id}/notes")
@ResponseStatus(HttpStatus.CREATED)
public NoteResource addNote(@PathVariable Long id, @RequestBody NewNote req){
 return ns.addNote(id, req.text());
}
}
