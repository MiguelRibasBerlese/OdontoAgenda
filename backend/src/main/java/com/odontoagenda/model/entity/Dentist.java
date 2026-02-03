package com.odontoagenda.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um dentista.
 * Dentista é um tipo de usuário com perfil DENTIST.
 */
@Entity
@Table(name = "dentists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dentist {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false, unique = true, length = 20)
    private String cro;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "dentist_specialties",
        joinColumns = @JoinColumn(name = "dentist_id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<Specialty> specialties = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String bio;
}
