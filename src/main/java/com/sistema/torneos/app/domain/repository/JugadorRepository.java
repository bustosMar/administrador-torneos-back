package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Jugador;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
	
	Jugador findByNombreAndApellidoAndFechaNacimiento(String nombre, String apellido, LocalDate fechaNacimiento);
	
	List<Jugador> findByNombreContainingIgnoreCase(String nombre);

	List<Jugador>
    findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre,
            String apellido
    );
	
	 List<Jugador>
	    findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCase(
	            String nombre,
	            String apellido
	    );
	 
	 List<Jugador>
	    findByApellidoContainingIgnoreCaseAndNombreContainingIgnoreCase(
	            String apellido,
	            String nombre
	    );
	 
	

}
