package com.odontoagenda.repository;

import com.odontoagenda.model.entity.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {
    
    Optional<Dentist> findByUserEmail(String email);
    
    @Query("SELECT d FROM Dentist d WHERE d.user.active = true")
    List<Dentist> findAllActive();
    
    @Query("SELECT d FROM Dentist d JOIN d.specialties s WHERE s.id = :specialtyId AND d.user.active = true")
    List<Dentist> findBySpecialtyId(Long specialtyId);
}
