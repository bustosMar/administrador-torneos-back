package com.sistema.torneos.app.domain.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "vigencias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vigencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String clave;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;

    @Column(nullable = false)
    private boolean activa = true;

    @Column(name = "fecha_Creacion")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime creadoEn;

}