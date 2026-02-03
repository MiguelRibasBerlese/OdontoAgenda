package com.odontoagenda.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa uma especialidade odontológica.
 */
@Entity
@Table(name = "specialties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Duração padrão de uma consulta desta especialidade em minutos.
     */
    @Column(nullable = false)
    private Integer defaultDurationMinutes;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToMany(mappedBy = "specialties")
    private Set<Dentist> dentists = new HashSet<>();
}
