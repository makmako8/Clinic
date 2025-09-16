package com.example.clinic.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.clinic.domain.Note;
import com.example.clinic.dto.NoteResource;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper {
NoteResource toResource(Note entity);
Note toEntity(NoteResource dto);
}
