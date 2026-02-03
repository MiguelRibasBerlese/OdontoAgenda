package com.odontoagenda.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para requisição de criação de dentista.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDentistRequest {

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
    private String fullName;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 100, message = "E-mail não pode ter mais de 100 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String password;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "Telefone não pode ter mais de 20 caracteres")
    private String phone;

    @NotBlank(message = "CRO é obrigatório")
    @Size(max = 20, message = "CRO não pode ter mais de 20 caracteres")
    private String cro;

    @NotEmpty(message = "Pelo menos uma especialidade deve ser selecionada")
    private Set<Long> specialtyIds;

    private String bio;
}
