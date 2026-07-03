package com.sistema.torneos.app.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "edad_minima")
    private Integer edadMinima;

    @Column(name = "edad_maxima")
    private Integer edadMaxima;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "permitir_inscripcion", nullable = false)
    @Deprecated // Ya no se usa en la lógica de validación. La validación ahora usa combinaciones de categorías (Primera+Veteranos, Segunda+Veteranos)
    private boolean permitirInscripcion = false;

    @Column(name = "activa", nullable = false)
    private boolean activa = true;
}
