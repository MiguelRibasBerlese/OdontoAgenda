package com.odontoagenda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de criação de especialidade.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpecialtyRequest {

    @NotBlank(message = "Nome da especialidade é obrigatório")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
    private String name;

    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    private String description;

    @NotNull(message = "Duração padrão é obrigatória")
    @Positive(message = "Duração deve ser positiva")
    private Integer defaultDurationMinutes;
}
