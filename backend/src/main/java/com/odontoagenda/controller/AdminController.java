package com.odontoagenda.controller;

import com.odontoagenda.dto.request.CreateDentistRequest;
import com.odontoagenda.dto.request.CreateSpecialtyRequest;
import com.odontoagenda.dto.response.DentistResponse;
import com.odontoagenda.dto.response.SpecialtyResponse;
import com.odontoagenda.service.DentistService;
import com.odontoagenda.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações administrativas.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DentistService dentistService;
    private final SpecialtyService specialtyService;

    // ==================== Especialidades ====================

    @PostMapping("/specialties")
    public ResponseEntity<SpecialtyResponse> createSpecialty(@Valid @RequestBody CreateSpecialtyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.createSpecialty(request));
    }

    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<Void> deactivateSpecialty(@PathVariable Long id) {
        specialtyService.deactivateSpecialty(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Dentistas ====================

    @PostMapping("/dentists")
    public ResponseEntity<DentistResponse> createDentist(@Valid @RequestBody CreateDentistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dentistService.createDentist(request));
    }

    @GetMapping("/dentists")
    public ResponseEntity<List<DentistResponse>> listDentists() {
        return ResponseEntity.ok(dentistService.listActiveDentists());
    }

    @DeleteMapping("/dentists/{id}")
    public ResponseEntity<Void> deactivateDentist(@PathVariable Long id) {
        dentistService.deactivateDentist(id);
        return ResponseEntity.noContent().build();
    }
}
