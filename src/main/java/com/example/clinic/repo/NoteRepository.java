package com.example.clinic.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic.domain.Note;
public interface NoteRepository extends JpaRepository<Note, Long> {
}
