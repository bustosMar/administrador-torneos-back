package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Rol, Long> {
}
