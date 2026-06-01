package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.domain.repository.GolRepository;
import com.sistema.torneos.app.service.GolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GolServiceImpl implements GolService {

    private final GolRepository repository;

    public GolServiceImpl(GolRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Gol> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Gol findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Gol create(Gol gol) {
        return repository.save(gol);
    }

    @Override
    @Transactional
    public Gol update(Long id, Gol gol) {
        Gol existing = findById(id);
        if (existing != null) {
            existing.setMinuto(gol.getMinuto());
            existing.setTipo(gol.getTipo());
            existing.setJugador(gol.getJugador());
            existing.setEquipoTorneo(gol.getEquipoTorneo());
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
