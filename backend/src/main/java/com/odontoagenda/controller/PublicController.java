package com.odontoagenda.controller;

import com.odontoagenda.dto.response.DentistResponse;
import com.odontoagenda.dto.response.SpecialtyResponse;
import com.odontoagenda.service.DentistService;
import com.odontoagenda.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para endpoints públicos (sem autenticação).
 */
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final SpecialtyService specialtyService;
    private final DentistService dentistService;

    /**
     * Lista todas as especialidades disponíveis.
     */
    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyResponse>> listSpecialties() {
        return ResponseEntity.ok(specialtyService.listActiveSpecialties());
    }

    /**
     * Lista todos os dentistas ativos.
     */
    @GetMapping("/dentists")
    public ResponseEntity<List<DentistResponse>> listDentists() {
        return ResponseEntity.ok(dentistService.listActiveDentists());
    }

    /**
     * Lista dentistas por especialidade.
     */
    @GetMapping("/dentists/specialty/{specialtyId}")
    public ResponseEntity<List<DentistResponse>> listDentistsBySpecialty(@PathVariable Long specialtyId) {
        return ResponseEntity.ok(dentistService.listDentistsBySpecialty(specialtyId));
    }
}
