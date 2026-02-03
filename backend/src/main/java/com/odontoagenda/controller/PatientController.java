package com.odontoagenda.controller;

import com.odontoagenda.dto.request.CreateAppointmentRequest;
import com.odontoagenda.dto.response.AppointmentResponse;
import com.odontoagenda.model.entity.User;
import com.odontoagenda.model.enums.UserRole;
import com.odontoagenda.repository.PatientRepository;
import com.odontoagenda.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller com endpoints para pacientes.
 */
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

    /**
     * Cria um novo agendamento para o paciente autenticado.
     */
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateAppointmentRequest request) {
        
        Long patientId = patientRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"))
                .getId();
        
        AppointmentResponse response = appointmentService.createAppointment(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista as próximas consultas do paciente autenticado.
     */
    @GetMapping("/appointments/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments(
            @AuthenticationPrincipal User user) {
        
        Long patientId = patientRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"))
                .getId();
        
        List<AppointmentResponse> appointments = appointmentService.getUpcomingAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Lista todos os agendamentos do paciente autenticado.
     */
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments(
            @AuthenticationPrincipal User user) {
        
        Long patientId = patientRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"))
                .getId();
        
        List<AppointmentResponse> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Cancela um agendamento.
     */
    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        
        AppointmentResponse response = appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok(response);
    }
}
