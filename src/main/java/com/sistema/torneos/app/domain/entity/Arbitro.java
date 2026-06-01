package com.sistema.torneos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "arbitros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arbitro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @OneToMany(mappedBy = "arbitro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Partido> partidos = new HashSet<>();
}
