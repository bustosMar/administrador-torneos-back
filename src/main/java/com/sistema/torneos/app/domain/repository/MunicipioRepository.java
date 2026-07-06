package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Municipio findByNombre(String nombre);
}
