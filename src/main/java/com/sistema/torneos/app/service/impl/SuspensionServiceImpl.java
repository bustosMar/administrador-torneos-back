package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Suspension;
import com.sistema.torneos.app.domain.repository.SuspensionRepository;
import com.sistema.torneos.app.service.SuspensionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SuspensionServiceImpl implements SuspensionService {

    private final SuspensionRepository repository;

    public SuspensionServiceImpl(SuspensionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suspension> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Suspension findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Suspension create(Suspension suspension) {
        return repository.save(suspension);
    }

    @Override
    @Transactional
    public Suspension update(Long id, Suspension suspension) {
        Suspension existing = findById(id);
        if (existing != null) {
            existing.setFechaInicio(suspension.getFechaInicio());
            existing.setFechaFin(suspension.getFechaFin());
            existing.setMotivo(suspension.getMotivo());
            existing.setJugador(suspension.getJugador());
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
