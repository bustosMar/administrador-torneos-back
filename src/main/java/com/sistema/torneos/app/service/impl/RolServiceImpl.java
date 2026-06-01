package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.domain.repository.RoleRepository;
import com.sistema.torneos.app.service.RolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    private final RoleRepository repository;

    public RolServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Rol create(Rol rol) {
        return repository.save(rol);
    }

    @Override
    @Transactional
    public Rol update(Long id, Rol rol) {
        Rol existing = findById(id);
        if (existing != null) {
            existing.setNombre(rol.getNombre());
            return repository.save(existing);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
