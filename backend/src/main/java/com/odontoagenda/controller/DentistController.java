package com.odontoagenda.controller;

import com.odontoagenda.dto.request.RescheduleAppointmentRequest;
import com.odontoagenda.dto.request.UpdateAppointmentStatusRequest;
import com.odontoagenda.dto.response.AppointmentResponse;
import com.odontoagenda.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller para operações de dentista.
 */
@RestController
@RequestMapping("/api/dentist")
@PreAuthorize("hasAnyRole('DENTIST', 'ADMIN')")
@RequiredArgsConstructor
public class DentistController {

    private final AppointmentService appointmentService;

    /**
     * Lista consultas do dentista em um período.
     */
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> listAppointments(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(appointmentService.listDentistAppointments(
                userDetails.getUsername(),
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        ));
    }

    /**
     * Atualiza status de uma consulta.
     */
    @PutMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentStatusRequest request
    ) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(
                appointmentId,
                request.getStatus(),
                request.getReason()
        ));
    }

    /**
     * Remarca uma consulta.
     */
    @PutMapping("/appointments/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody RescheduleAppointmentRequest request
    ) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(
                appointmentId,
                request.getNewAppointmentDateTime(),
                request.getReason()
        ));
    }
}
