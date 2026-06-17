package com.sistema.torneos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jornadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de jornada dentro del torneo
     */
    @Column(name = "numero_jornada", nullable = false)
    private Integer numeroJornada;

    /**
     * Estado: PROGRAMADA, EN_CURSO, FINALIZADA
     */
    @Column(name = "estado", nullable = false)
    private String estado;

    /**
     * Relación con torneo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo", nullable = false)
    @JsonIgnore
    private Torneo torneo;

    /**
     * Opcional: grupo (si manejas torneos por grupos independientes)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo")
    @JsonIgnore
    private Grupo grupo;

    /**
     * Fecha tentativa de la jornada
     */
    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    /**
     * RELACIÓN CON PARTIDOS
     */
    @OneToMany(mappedBy = "jornada", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Partido> partidos = new ArrayList<>();
}