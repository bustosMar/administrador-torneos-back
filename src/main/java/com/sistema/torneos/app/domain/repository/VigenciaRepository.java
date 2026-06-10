package com.sistema.torneos.app.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.torneos.app.domain.entity.Vigencia;

import java.time.LocalDate;
import java.util.Optional;

public interface VigenciaRepository extends JpaRepository<Vigencia, Long> {
  
	@Query("""
		    SELECT v
		    FROM Vigencia v
		    WHERE v.activa = true
		      AND v.clave = :clave
		      AND :fecha BETWEEN v.fechaInicio AND v.fechaFin
		""")
		Optional<Vigencia> findVigenciaActiva(
		        @Param("clave") String clave,
		        @Param("fecha") LocalDate fecha
		);

}
