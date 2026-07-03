package com.sistema.torneos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "jugador_en_categoria", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_jugador", "id_categoria_torneo"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JugadorEnCategoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_torneo", nullable = false)
    @JsonIgnore
    private CategoriaTorneo categoriaTorneo;

    @Column(name = "fecha_inscripcion")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInscripcion = LocalDate.now();

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
