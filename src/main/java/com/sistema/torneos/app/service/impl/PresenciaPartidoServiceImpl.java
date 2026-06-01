package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import com.sistema.torneos.app.domain.repository.PresenciaPartidoRepository;
import com.sistema.torneos.app.service.PresenciaPartidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PresenciaPartidoServiceImpl implements PresenciaPartidoService {

    private final PresenciaPartidoRepository repository;

    public PresenciaPartidoServiceImpl(PresenciaPartidoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresenciaPartido> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PresenciaPartido findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PresenciaPartido create(PresenciaPartido presenciaPartido) {
        return repository.save(presenciaPartido);
    }

    @Override
    @Transactional
    public PresenciaPartido update(Long id, PresenciaPartido presenciaPartido) {
        PresenciaPartido existing = findById(id);
        if (existing != null) {
            existing.setObservaciones(presenciaPartido.getObservaciones());
            existing.setJugador(presenciaPartido.getJugador());
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
