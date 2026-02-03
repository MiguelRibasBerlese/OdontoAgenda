package com.odontoagenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para resposta de dados de dentista.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DentistResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String cro;
    private String bio;
    private Set<SpecialtyResponse> specialties;
}
