package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JugadorEnEquipoRepository extends JpaRepository<JugadorEnEquipo, Long> {

	@Query("SELECT j FROM JugadorEnEquipo j WHERE j.torneo.activo = true")
	List<JugadorEnEquipo> findAllByTorneoActivo();

	@Query("""
			SELECT je
			FROM JugadorEnEquipo je
			JOIN FETCH je.jugador j
			JOIN FETCH je.equipo e
			WHERE je.activo = true
			AND je.torneo.id = :torneoId
			AND je.categoriaTorneo.id = :categoriaTorneoId
			AND e.id IN :equipoIds
			ORDER BY e.nombre ASC, j.nombre ASC, j.apellido ASC
			""")
	List<JugadorEnEquipo> findActivosByEquiposTorneoYCategoria(
	        @Param("torneoId") Long torneoId,
	        @Param("categoriaTorneoId") Long categoriaTorneoId,
	        @Param("equipoIds") List<Long> equipoIds);

	@Query("""
			SELECT je
			FROM JugadorEnEquipo je
			JOIN FETCH je.jugador j
			JOIN FETCH je.equipo e
			JOIN FETCH je.torneo t
			LEFT JOIN FETCH t.municipioEstado me
			WHERE je.activo = true
			AND t.id = :torneoId
			AND j.huella = :huella
			ORDER BY e.nombre ASC, j.nombre ASC, j.apellido ASC
			""")
	List<JugadorEnEquipo> findActivosByTorneoAndHuella(
	        @Param("torneoId") Long torneoId,
	        @Param("huella") String huella);
	
	 JugadorEnEquipo findByJugador_IdAndTorneo_IdAndActivo(
	            Long idJugador,
	            Long idTorneo,
	            boolean activo
	    );
}