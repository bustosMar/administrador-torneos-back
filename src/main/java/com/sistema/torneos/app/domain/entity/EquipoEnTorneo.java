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
@Table(name = "equipos_en_torneo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoEnTorneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    @JsonIgnore
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo", nullable = false)
    @JsonIgnore
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo")
    @JsonIgnore
    private Grupo grupo;

    @Column(name = "fecha_Creacion")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate creadoEn = LocalDate.now();

    @OneToMany(mappedBy = "equipoTorneo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Gol> goles = new HashSet<>();
}
