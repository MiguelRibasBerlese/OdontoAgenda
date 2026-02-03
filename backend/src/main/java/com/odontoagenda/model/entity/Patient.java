package com.odontoagenda.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um paciente.
 * Paciente é um tipo de usuário com perfil PATIENT.
 */
@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(length = 14)
    private String cpf;

    @Column(length = 200)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
}
