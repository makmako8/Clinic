package com.example.clinic.web;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinic.dto.PatientResource;
import com.example.clinic.service.PatientService;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patients")
public class PatientController {

  private final PatientService service;

  public static record NewPatient(
	      @NotBlank @JsonAlias("mrn") String identifier,
	      @NotBlank String name,
	      @JsonAlias("sex") String gender // null 可。male/female/other/unknown を想定
	  ) {}

	  @PostMapping
	  @ResponseStatus(HttpStatus.CREATED)
	  public PatientResource create(@RequestBody NewPatient req) {
	    return service.create(req.identifier(), req.name(), req.gender());
	  }
	  
	    // 一覧/検索
	    @GetMapping("/api/patients")
	    public List<PatientResource> search(@RequestParam(name = "q", required = false) String q) {
	        return service.search(q);
	    }

	    // 単一取得（任意）
	    @GetMapping("/api/patients/{id}")
	    public PatientResource get(@PathVariable Long id) {
	        return service.get(id);
	    }
}