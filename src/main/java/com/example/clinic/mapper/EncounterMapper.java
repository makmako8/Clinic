package com.example.clinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.example.clinic.domain.Encounter;
import com.example.clinic.dto.EncounterResource;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EncounterMapper {

@Mappings({
   @Mapping(target = "tenantId", ignore = true),
   @Mapping(target = "createdAt", ignore = true),
   @Mapping(target = "updatedAt", ignore = true),
   @Mapping(target = "doctorUserId", ignore = true),
   // DTO(OffsetDateTime) -> Entity(LocalDateTime)

})
Encounter toEntity(EncounterResource src);


EncounterResource toResource(Encounter entity);


}
