package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresenciaPartidoRepository extends JpaRepository<PresenciaPartido, Long> {

	List<PresenciaPartido> findByPartido_IdOrderByJugador_NombreAscJugador_ApellidoAsc(Long idPartido);

	Optional<PresenciaPartido> findByPartido_IdAndJugador_Id(Long idPartido, Long idJugador);
}
