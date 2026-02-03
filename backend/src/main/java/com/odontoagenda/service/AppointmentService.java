package com.odontoagenda.service;

import com.odontoagenda.dto.request.CreateAppointmentRequest;
import com.odontoagenda.dto.response.AppointmentResponse;
import com.odontoagenda.exception.BusinessException;
import com.odontoagenda.exception.ResourceNotFoundException;
import com.odontoagenda.model.entity.Appointment;
import com.odontoagenda.model.entity.Dentist;
import com.odontoagenda.model.entity.Patient;
import com.odontoagenda.model.entity.Specialty;
import com.odontoagenda.model.enums.AppointmentStatus;
import com.odontoagenda.repository.AppointmentRepository;
import com.odontoagenda.repository.DentistRepository;
import com.odontoagenda.repository.PatientRepository;
import com.odontoagenda.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo gerenciamento de agendamentos.
 * Implementa as regras de negócio para criação, atualização e consulta de consultas.
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final SpecialtyRepository specialtyRepository;

    /**
     * Cria um novo agendamento.
     * Valida data futura, conflito de horário e disponibilidade.
     */
    @Transactional
    public AppointmentResponse createAppointment(Long patientId, CreateAppointmentRequest request) {
        // Busca as entidades
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Dentist dentist = dentistRepository.findById(request.getDentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado"));

        Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada"));

        // Valida se a data está no futuro
        if (request.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar em data/hora passada");
        }

        // Calcula horário de término
        LocalDateTime endTime = request.getAppointmentDateTime()
                .plusMinutes(specialty.getDefaultDurationMinutes());

        // Verifica conflito de horário
        if (appointmentRepository.existsConflict(
                request.getDentistId(),
                request.getAppointmentDateTime(),
                endTime)) {
            throw new BusinessException("Dentista já possui agendamento neste horário");
        }

        // Cria o agendamento
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .dentist(dentist)
                .specialty(specialty)
                .appointmentDateTime(request.getAppointmentDateTime())
                .durationMinutes(specialty.getDefaultDurationMinutes())
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                .build();

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }

    /**
     * Lista as próximas consultas de um paciente.
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingByPatientId(
                patientId, 
                LocalDateTime.now()
        );
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os agendamentos de um paciente.
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista os agendamentos de um dentista em um intervalo de datas.
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDentistAppointments(Long dentistId, 
                                                            LocalDateTime startDate, 
                                                            LocalDateTime endDate) {
        List<Appointment> appointments = appointmentRepository.findByDentistIdAndDateRange(
                dentistId, 
                startDate, 
                endDate
        );
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista agendamentos de dentista por email (usado pelo controller).
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> listDentistAppointments(String dentistEmail, 
                                                             LocalDateTime startDate, 
                                                             LocalDateTime endDate) {
        Dentist dentist = dentistRepository.findByUserEmail(dentistEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado"));
        
        return getDentistAppointments(dentist.getId(), startDate, endDate);
    }

    /**
     * Atualiza o status de um agendamento.
     */
    @Transactional
    public AppointmentResponse updateStatus(Long appointmentId, AppointmentStatus newStatus, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        appointment.setStatus(newStatus);
        appointment.setLastModifiedAt(LocalDateTime.now());
        
        if (newStatus == AppointmentStatus.CANCELED && reason != null) {
            appointment.setCancellationReason(reason);
        }

        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    /**
     * Atualiza status de agendamento (interface simplificada).
     */
    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, 
                                                       AppointmentStatus status, 
                                                       String reason) {
        return updateStatus(appointmentId, status, reason);
    }

    /**
     * Remarca um agendamento para nova data/hora.
     */
    @Transactional
    public AppointmentResponse rescheduleAppointment(Long appointmentId, 
                                                     LocalDateTime newDateTime, 
                                                     String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        // Valida se a nova data está no futuro
        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível remarcar para data/hora passada");
        }

        // Calcula novo horário de término
        LocalDateTime endTime = newDateTime.plusMinutes(appointment.getDurationMinutes());

        // Verifica conflito de horário
        if (appointmentRepository.existsConflict(
                appointment.getDentist().getId(),
                newDateTime,
                endTime)) {
            throw new BusinessException("Dentista já possui agendamento neste horário");
        }

        // Atualiza o agendamento
        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        appointment.setLastModifiedAt(LocalDateTime.now());
        appointment.setCancellationReason(reason);

        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    /**
     * Cancela um agendamento (apenas se respeitar regras de antecedência).
     */
    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        // Regra: pode cancelar até 24 horas antes
        if (appointment.getAppointmentDateTime().minusHours(24).isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cancelamento deve ser feito com no mínimo 24 horas de antecedência");
        }

        return updateStatus(appointmentId, AppointmentStatus.CANCELED, reason);
    }

    /**
     * Mapeia entidade para DTO de resposta.
     */
    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getUser().getFullName())
                .dentistId(appointment.getDentist().getId())
                .dentistName(appointment.getDentist().getUser().getFullName())
                .specialtyId(appointment.getSpecialty().getId())
                .specialtyName(appointment.getSpecialty().getName())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .durationMinutes(appointment.getDurationMinutes())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}
