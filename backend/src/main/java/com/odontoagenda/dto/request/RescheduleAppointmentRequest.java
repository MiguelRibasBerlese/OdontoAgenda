package com.odontoagenda.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para requisição de remarcação de agendamento.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleAppointmentRequest {

    @NotNull(message = "Nova data e hora são obrigatórias")
    @Future(message = "Nova data e hora devem estar no futuro")
    private LocalDateTime newAppointmentDateTime;

    private String reason;
}
