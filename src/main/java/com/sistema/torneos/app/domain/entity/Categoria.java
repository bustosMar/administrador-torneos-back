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

    @Column(name = "edad_minima", nullable = true)
    private Integer edadMinima;

    @Column(name = "edad_maxima", nullable = true)
    private Integer edadMaxima;

    @Column(name = "activa", nullable = false)
    private boolean activa = true;
}
