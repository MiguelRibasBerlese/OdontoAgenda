package com.odontoagenda.service;

import com.odontoagenda.dto.response.SpecialtyResponse;
import com.odontoagenda.dto.request.CreateSpecialtyRequest;
import com.odontoagenda.exception.BusinessException;
import com.odontoagenda.exception.ResourceNotFoundException;
import com.odontoagenda.model.entity.Specialty;
import com.odontoagenda.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável pela lógica de negócio de especialidades.
 */
@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    /**
     * Lista todas as especialidades ativas.
     */
    @Transactional(readOnly = true)
    public List<SpecialtyResponse> listActiveSpecialties() {
        return specialtyRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cria uma nova especialidade.
     */
    @Transactional
    public SpecialtyResponse createSpecialty(CreateSpecialtyRequest request) {
        // Valida se já existe especialidade com o mesmo nome
        if (specialtyRepository.existsByName(request.getName())) {
            throw new BusinessException("Já existe uma especialidade com este nome");
        }

        Specialty specialty = Specialty.builder()
                .name(request.getName())
                .description(request.getDescription())
                .defaultDurationMinutes(request.getDefaultDurationMinutes())
                .active(true)
                .build();

        specialty = specialtyRepository.save(specialty);
        return toResponse(specialty);
    }

    /**
     * Busca especialidade por ID.
     */
    @Transactional(readOnly = true)
    public Specialty findById(Long id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada"));
    }

    /**
     * Desativa uma especialidade.
     */
    @Transactional
    public void deactivateSpecialty(Long id) {
        Specialty specialty = findById(id);
        specialty.setActive(false);
        specialtyRepository.save(specialty);
    }

    private SpecialtyResponse toResponse(Specialty specialty) {
        return SpecialtyResponse.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .description(specialty.getDescription())
                .defaultDurationMinutes(specialty.getDefaultDurationMinutes())
                .build();
    }
}
