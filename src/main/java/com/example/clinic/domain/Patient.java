package com.example.clinic.domain;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="patient",
indexes = { @Index(columnList="tenantId"), @Index(columnList="mrn"), @Index(columnList="name") })
@Getter @Setter @NoArgsConstructor
public class Patient extends BaseEntity {
	
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "tenant_id", nullable = false)
private Long tenantId;
	
@Column(name = "mrn", nullable = false, length = 64)
private String mrn; // 院内ID

@Column(name = "name", nullable = false, length = 200)
private String name;

@Enumerated(EnumType.STRING)
@Column(name = "sex", length = 16)
private Sex sex; // "male" | "female" | "other" | "unknown"

@Column(name = "created_at", nullable = false)
private Instant createdAt;

@Column(name = "updated_at", nullable = false)
private Instant updatedAt;

private LocalDate birthDate;


@Column(length=256) private String address;
@Column(length=32) private String phone;
@Column(length=256) private String emergencyContact;
@Column(length=512) private String consentFlagsJson; // 将来は正規化/FHIR Consentへ
@PrePersist
public void prePersist() {
  if (createdAt == null) createdAt = Instant.now();
  if (updatedAt == null) updatedAt = createdAt;
}
public Long getId() { return id; }
public void setId(Long id) { this.id = id; }

public String getMrn() {
    return mrn;
}
public void setMrn(String mrn) {
    this.mrn = mrn;
}
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public LocalDate getBirthDate() {
    return birthDate;
}
public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
}
public Sex getSex() {
    return sex;
}
public void setSex(Sex sex) {
    this.sex = sex;
}
public Instant getCreatedAt() { return createdAt; }
public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
public Instant getUpdatedAt() { return updatedAt; }
public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

public Long getTenantId() { return tenantId; }
public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
public String getAddress() {
    return address;
}
public void setAddress(String address) {
    this.address = address;
}
public String getPhone() {
    return phone;
}
public void setPhone(String phone) {
    this.phone = phone;
}
public String getEmergencyContact() {
    return emergencyContact;
}
public void setEmergencyContact(String emergencyContact) {
    this.emergencyContact = emergencyContact;
}
public String getConsentFlagsJson() {
    return consentFlagsJson;
}
public void setConsentFlagJson(String consentFlagsJson) {
    this.consentFlagsJson = consentFlagsJson;
}

@PreUpdate
public void preUpdate() { updatedAt = Instant.now(); }
}
