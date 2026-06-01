package com.sistema.torneos.app.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Size(min=4, max = 12)
    private String nombreUsuario;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean administrador;

    @NotBlank
    private String password;

    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="usuarios_roles",
        joinColumns = {@JoinColumn(name="id_usuario")},
        inverseJoinColumns = @JoinColumn(name="id_rol"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"id_usuario", "id_rol"})}
    )
    private List<Rol> roles = new ArrayList<>();
    
}
