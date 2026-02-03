package com.odontoagenda.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para requisição de criação de agendamento.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {

    @NotNull(message = "ID do dentista é obrigatório")
    @Positive(message = "ID do dentista deve ser positivo")
    private Long dentistId;

    @NotNull(message = "ID da especialidade é obrigatório")
    @Positive(message = "ID da especialidade deve ser positivo")
    private Long specialtyId;

    @NotNull(message = "Data e hora do agendamento são obrigatórios")
    @Future(message = "Data e hora devem estar no futuro")
    private LocalDateTime appointmentDateTime;

    private String notes;
}
