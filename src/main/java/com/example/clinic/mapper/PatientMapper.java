package com.example.clinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.example.clinic.domain.Patient;
import com.example.clinic.domain.Sex;
import com.example.clinic.dto.PatientResource;

@Mapper(
	    componentModel = "spring",
	    unmappedTargetPolicy = ReportingPolicy.IGNORE // マッピング漏れの警告を黙らせる
	)
public interface PatientMapper {

@Mappings({
   @Mapping(target = "mrn", source = "identifier"),
   @Mapping(target = "sex", source = "gender"),
   
   // ここはサービス層でセットするので MapStruct では無視
   @Mapping(target = "id", ignore = true),
   @Mapping(target = "tenantId", ignore = true),
   @Mapping(target = "createdAt", ignore = true),
   @Mapping(target = "updatedAt", ignore = true),
   
})
Patient toEntity(PatientResource src);

@Mappings({
   @Mapping(target = "identifier", source = "mrn"),
   @Mapping(target = "gender", source = "sex")
})
PatientResource toResource(Patient entity);

@Named("toSex")
default Sex toSex(String gender) {
  if (gender == null) return null;
  return switch (gender.toLowerCase()) {
    case "male" -> Sex.MALE;
    case "female" -> Sex.FEMALE;
    case "other" -> Sex.OTHER;
    case "unknown" -> Sex.UNKNOWN;
    default -> throw new IllegalArgumentException("Unsupported gender: " + gender);
  };
}

@Named("fromSex")
default String fromSex(Sex sex) {
  if (sex == null) return null;
  return switch (sex) {
    case MALE -> "male";
    case FEMALE -> "female";
    case OTHER -> "other";
    case UNKNOWN -> "unknown";
  };
}

}
