package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Estado findByNombre(String nombre);
}
