package com.example.clinic.domain;



import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "note")
@Getter @Setter
public class Note {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tenant_id", nullable = false)
  private Long tenantId;

  @Column(name = "encounter_id", nullable = false)
  private Long encounterId;

  @Column(name = "author_user_id")
  private String authorUserId;

  @Column(name = "author_username", length = 100)
  private String authorUsername;

  @Lob
  @Column(name = "text", nullable = false)
  private String text;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (createdAt == null) createdAt = Instant.now();
  }
  
	public Long getTenantId() {
	    return tenantId;
	}
	public void setTenant(Long tenantId) {
	    this.tenantId = tenantId;
	}
	
	public Long getEncounterId() {
	    return encounterId;
	}
	public void setEncounter(Long encounterId) {
	    this.encounterId = encounterId;
	}
	public String getAuthorUserId() {
	    return authorUserId;
	}
	public void setAuthorUserId(String authorUserId) {
	    this.authorUserId = authorUserId;
	}
	public String getAuthorUsername() {
	    return authorUsername;
	}
	public void setAuthorUsername(String authorUsername) {
	    this.authorUsername = authorUsername;
	}
	public String getText() {
	    return text;
	}
	public void setText(String text) {
	    this.text = text;
	}
}
