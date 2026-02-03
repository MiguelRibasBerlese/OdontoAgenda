package com.odontoagenda.service;

import com.odontoagenda.dto.request.CreateDentistRequest;
import com.odontoagenda.dto.response.DentistResponse;
import com.odontoagenda.dto.response.SpecialtyResponse;
import com.odontoagenda.exception.BusinessException;
import com.odontoagenda.exception.ResourceNotFoundException;
import com.odontoagenda.model.entity.Dentist;
import com.odontoagenda.model.entity.Specialty;
import com.odontoagenda.model.entity.User;
import com.odontoagenda.model.enums.UserRole;
import com.odontoagenda.repository.DentistRepository;
import com.odontoagenda.repository.SpecialtyRepository;
import com.odontoagenda.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service responsável pela lógica de negócio de dentistas.
 */
@Service
@RequiredArgsConstructor
public class DentistService {

    private final DentistRepository dentistRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lista todos os dentistas ativos.
     */
    @Transactional(readOnly = true)
    public List<DentistResponse> listActiveDentists() {
        return dentistRepository.findAllActive()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista dentistas por especialidade.
     */
    @Transactional(readOnly = true)
    public List<DentistResponse> listDentistsBySpecialty(Long specialtyId) {
        return dentistRepository.findBySpecialtyId(specialtyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cria um novo dentista.
     */
    @Transactional
    public DentistResponse createDentist(CreateDentistRequest request) {
        // Valida e-mail único
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        // Valida CRO único
        if (dentistRepository.existsByCro(request.getCro())) {
            throw new BusinessException("CRO já cadastrado");
        }

        // Busca especialidades
        Set<Specialty> specialties = new HashSet<>();
        for (Long specialtyId : request.getSpecialtyIds()) {
            Specialty specialty = specialtyRepository.findById(specialtyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada: " + specialtyId));
            specialties.add(specialty);
        }

        // Cria usuário
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(UserRole.DENTIST)
                .active(true)
                .build();

        user = userRepository.save(user);

        // Cria dentista
        Dentist dentist = Dentist.builder()
                .user(user)
                .cro(request.getCro())
                .bio(request.getBio())
                .specialties(specialties)
                .build();

        dentist = dentistRepository.save(dentist);
        return toResponse(dentist);
    }

    /**
     * Busca dentista por ID.
     */
    @Transactional(readOnly = true)
    public Dentist findById(Long id) {
        return dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado"));
    }

    /**
     * Desativa um dentista.
     */
    @Transactional
    public void deactivateDentist(Long id) {
        Dentist dentist = findById(id);
        dentist.getUser().setActive(false);
        userRepository.save(dentist.getUser());
    }

    private DentistResponse toResponse(Dentist dentist) {
        return DentistResponse.builder()
                .id(dentist.getId())
                .fullName(dentist.getUser().getFullName())
                .email(dentist.getUser().getEmail())
                .phone(dentist.getUser().getPhone())
                .cro(dentist.getCro())
                .bio(dentist.getBio())
                .specialties(dentist.getSpecialties().stream()
                        .map(s -> SpecialtyResponse.builder()
                                .id(s.getId())
                                .name(s.getName())
                                .description(s.getDescription())
                                .defaultDurationMinutes(s.getDefaultDurationMinutes())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
