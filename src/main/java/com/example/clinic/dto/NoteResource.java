package com.example.clinic.dto;



import java.time.Instant;

import lombok.Data;

@Data
public class NoteResource {
private Long id;
private Long encounterId;
private String text;
private String authorUsername; // 表示用
private Long authorUserId;     // 任意
private Instant createdAt;
}
