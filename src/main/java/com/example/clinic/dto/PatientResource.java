package com.example.clinic.dto;

public record PatientResource(
	    Long id,
	    String identifier,
	    String name,
	    String gender
	) {}