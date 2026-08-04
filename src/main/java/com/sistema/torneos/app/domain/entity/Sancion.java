package com.sistema.torneos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sanciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partido", nullable = false)
    @JsonIgnore
    private Partido partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    @JsonIgnore
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo_torneo", nullable = false)
    @JsonIgnore
    private EquipoEnTorneo equipoTorneo;

    @Column(name = "minuto", nullable = false)
    private Integer minuto;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "observacion", length = 255)
    private String observacion;
}
