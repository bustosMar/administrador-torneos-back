package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.domain.repository.RoleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class RolFacade {

    private final RoleRepository roleRepository;

    @Autowired
    public RolFacade(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Rol> findAll() {
        return roleRepository.findAll();
    }

    public Rol findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Rol create(Rol rol) {
        return roleRepository.save(rol);
    }

    @Transactional
    public Rol update(Long id, Rol rol) {
        if (roleRepository.existsById(id)) {
            rol.setId(id);
            return roleRepository.save(rol);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        }
    }
}
