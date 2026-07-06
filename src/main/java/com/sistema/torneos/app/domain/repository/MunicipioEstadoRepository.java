package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.MunicipioEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioEstadoRepository extends JpaRepository<MunicipioEstado, Long> {
    
    @Query("SELECT me FROM MunicipioEstado me WHERE me.estado.id = :estadoId")
    List<MunicipioEstado> findByEstadoId(@Param("estadoId") Long estadoId);
    
    @Query("SELECT me FROM MunicipioEstado me WHERE me.municipio.id = :municipioId AND me.estado.id = :estadoId")
    Optional<MunicipioEstado> findByMunicipioIdAndEstadoId(@Param("municipioId") Long municipioId, @Param("estadoId") Long estadoId);
}
