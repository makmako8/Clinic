package com.example.clinic.dto;

import java.time.LocalDateTime;

public record EncounterResource(
	    Long id,
	    Long tenantId,
	    Long patientId,
	    LocalDateTime visitDateTime,
	    String department,
	    String reason,
	    String doctorUserId
	) {}