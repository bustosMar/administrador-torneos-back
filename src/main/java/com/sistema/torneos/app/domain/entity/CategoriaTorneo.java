package com.sistema.torneos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categoria_torneo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_torneo", "id_categoria"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTorneo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo", nullable = false)
    @JsonIgnore
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "activa", nullable = false)
    private boolean activa = true;

    @Column(name = "orden")
    private Integer orden;

    @OneToMany(mappedBy = "categoriaTorneo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<JugadorEnCategoria> jugadoresEnCategoria = new HashSet<>();
}
