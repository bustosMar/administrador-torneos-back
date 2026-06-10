package com.sistema.torneos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "partidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo", nullable = false)
    @JsonIgnore
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo")
    @JsonIgnore
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arbitro")
    @JsonIgnore
    private Arbitro arbitro;

    @Column(name = "fecha", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;
    
    @Column(name = "hora", nullable = false)
    private String hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo_en_torneo_local", nullable = false)
    @JsonIgnore
    private EquipoEnTorneo equipoLocal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo_en_torneo_visitante", nullable = false)
    @JsonIgnore
    private EquipoEnTorneo equipoVisitante;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PresenciaPartido> presencias = new HashSet<>();

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Gol> goles = new HashSet<>();
}
