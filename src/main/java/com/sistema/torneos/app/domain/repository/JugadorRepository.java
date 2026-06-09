package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Jugador;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
	
	Jugador findByNombreAndApellidoAndFechaNacimiento(String nombre, String apellido, Date fechaNacimiento);
}
