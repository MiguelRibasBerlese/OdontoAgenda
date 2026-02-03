package com.odontoagenda.repository;

import com.odontoagenda.model.entity.Appointment;
import com.odontoagenda.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByDentistId(Long dentistId);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
           "AND a.appointmentDateTime >= :startDate " +
           "ORDER BY a.appointmentDateTime")
    List<Appointment> findUpcomingByPatientId(@Param("patientId") Long patientId, 
                                               @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.dentist.id = :dentistId " +
           "AND a.appointmentDateTime BETWEEN :startDate AND :endDate " +
           "ORDER BY a.appointmentDateTime")
    List<Appointment> findByDentistIdAndDateRange(@Param("dentistId") Long dentistId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
    
    /**
     * Verifica se existe conflito de horário para o dentista.
     * Considera a duração das consultas.
     */
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.dentist.id = :dentistId " +
           "AND a.status NOT IN ('CANCELED', 'COMPLETED') " +
           "AND ((a.appointmentDateTime <= :startTime AND " +
           "      FUNCTION('TIMESTAMPADD', MINUTE, a.durationMinutes, a.appointmentDateTime) > :startTime) " +
           "OR (a.appointmentDateTime < :endTime AND " +
           "    FUNCTION('TIMESTAMPADD', MINUTE, a.durationMinutes, a.appointmentDateTime) >= :endTime) " +
           "OR (a.appointmentDateTime >= :startTime AND " +
           "    FUNCTION('TIMESTAMPADD', MINUTE, a.durationMinutes, a.appointmentDateTime) <= :endTime))")
    boolean existsConflict(@Param("dentistId") Long dentistId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);
    
    List<Appointment> findByStatus(AppointmentStatus status);
}
