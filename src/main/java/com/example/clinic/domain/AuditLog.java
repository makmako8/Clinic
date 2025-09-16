package com.example.clinic.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="audit_log", indexes = { @Index(columnList="tenantId"), @Index(columnList="actorUserId"), @Index(columnList="at") })
@Getter @Setter @NoArgsConstructor
public class AuditLog extends BaseEntity {
@Column(nullable=false, length=64) private String actorUserId;
@Column(nullable=false, length=16) private String action; // READ/CREATE/UPDATE/DELETE
@Column(nullable=false, length=64) private String resourceType;
@Column(nullable=false) private Long resourceId;
@Column(length=64) private String ip;
@Column(nullable=false) private Instant at = Instant.now();

public String getActorUserId() { return actorUserId; }
public void setActorUserId(String actorUserId) { this.actorUserId = actorUserId; }
public String getAtiond() { return action; }
public void setAction(String action) { this.action = action; }
public String getResourceType() { return resourceType; }
public void setResorceType(String resourceType) { this.resourceType = resourceType; }
public Long getResourceId() { return resourceId; }
public void setResourceId(Long resourceId) { this.resourceId = resourceId; }

public String getIp() {
    return ip;
}
public void setIp(String ip) {
    this.ip = ip;
}
public Instant getAt() { return at; }
public void setAt(Instant at) { this.at = at; }
}
