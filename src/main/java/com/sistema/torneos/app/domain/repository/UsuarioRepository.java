package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
    Optional<Usuario> findByNombreAndApellidoAndNombreUsuario(
    	    String nombre,
    	    String apellido,
    	    String nombreUsuario
    	);
}
