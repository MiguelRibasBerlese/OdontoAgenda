package com.odontoagenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de dados de especialidade.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyResponse {

    private Long id;
    private String name;
    private String description;
    private Integer defaultDurationMinutes;
}
