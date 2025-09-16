package com.example.clinic.domain;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "encounter")
public class Encounter{
	
	  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	
	  @Column(name = "tenant_Id", nullable = false)
	  private Long tenantId;
	  
 	  @Column(name = "patient_Id", nullable = false)
	  private Long patientId;
	  
	  @Column(name = "visit_datetime", nullable = false)
	  private LocalDateTime visitDateTime;
	  
	  @Column(name = "department", length = 100)
	  private String department;
	  
	  @Column(name = "reason", length = 400)
	  private String reason;
	  
	  @Column(name = "doctor_user_id")
	  private String doctorUserId; // JWT sub等を保存（将来Userエンティティでも可）

	  @Column(name = "created_at", nullable = false)
	  private Instant createdAt;

	  @Column(name = "updated_at", nullable = false)
	  private Instant updatedAt;
	  
	  
	  @PrePersist
	  void prePersist() {
	    Instant now = Instant.now();
	    if (createdAt == null) createdAt = now;
	    if (updatedAt == null) updatedAt = now;
	    // visitDateTime はサービス側で埋める前提（nullならNG）
	  }
	  
	  @PreUpdate
	  void preUpdate() {
	    updatedAt = Instant.now();
	  }
	public Long getPatientId() {
	    return patientId;
	}
	public void setPatientId(Long patientId) {
	    this.patientId = patientId;
	}
	public Long getTenantId() {
	    return tenantId;
	}
	public void setTenantId(Long tenantId) {
	    this.tenantId = tenantId;
	}
	
	public LocalDateTime getVisitDateTime() {
	    return visitDateTime;
	}

	public void setVisitDateTime(LocalDateTime visitDateTime) {
    this.visitDateTime = visitDateTime;
    }

	public String getDepartment() {
	    return department;
	}
	public void setDepartment(String department) {
	    this.department = department;
	}

public String getDoctorUserId() { return doctorUserId; }
public void setDoctorUserId(String doctorUserId) { this.doctorUserId = doctorUserId; }
public String getReason() {
    return reason;
}
public void setReason(String reason) {
    this.reason = reason;
}
public Instant getCreatedAt() { return createdAt; }
public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
public Instant getUpdatedAt() { return updatedAt; }
public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }




}
