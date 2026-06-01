package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.domain.repository.ArbitroRepository;
import com.sistema.torneos.app.service.ArbitroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArbitroServiceImpl implements ArbitroService {

    private final ArbitroRepository repository;

    public ArbitroServiceImpl(ArbitroRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Arbitro> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Arbitro findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Arbitro create(Arbitro arbitro) {
        return repository.save(arbitro);
    }

    @Override
    @Transactional
    public Arbitro update(Long id, Arbitro arbitro) {
        Arbitro existing = findById(id);
        if (existing != null) {
            existing.setNombre(arbitro.getNombre());
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
