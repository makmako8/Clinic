package com.example.clinic.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.clinic.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByIdAndTenantId(Long id, Long tenantId);

    Optional<Patient> findByIdAndTenantId(Long id, Long tenantId);

    @Query("""
        select p from Patient p
        where p.tenantId = :tenantId
          and ( lower(p.name) like lower(concat('%', :q, '%'))
             or lower(p.mrn)  like lower(concat('%', :q, '%')) )
              order by p.id desc
        """)
    List<Patient> search(@Param("tenantId") Long tenantId, @Param("q") String q);
}