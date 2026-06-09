package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Arbitro;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
	
	Arbitro findByNombreAndApellido(String nombre, String apellido);
}
