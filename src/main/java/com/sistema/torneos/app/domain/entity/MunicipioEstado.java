package com.sistema.torneos.app.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "municipio_estado",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_municipio","id_estado"}),
       indexes = {@Index(columnList = "id_municipio"), @Index(columnList = "id_estado")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MunicipioEstado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_municipio", nullable = false, foreignKey = @ForeignKey(name = "fk_municipioestado_municipio"))
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false, foreignKey = @ForeignKey(name = "fk_municipioestado_estado"))
    private Estado estado;
}
