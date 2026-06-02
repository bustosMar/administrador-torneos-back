package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.domain.repository.ArbitroRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class ArbitroFacade {

    private final ArbitroRepository arbitroRepository;

    @Autowired
    public ArbitroFacade(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public List<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }

    public Arbitro findById(Long id) {
        return arbitroRepository.findById(id).orElse(null);
    }

    @Transactional
    public Arbitro create(Arbitro arbitro) {
        return arbitroRepository.save(arbitro);
    }

    @Transactional
    public Arbitro update(Long id, Arbitro arbitro) {
        if (arbitroRepository.existsById(id)) {
            arbitro.setId(id);
            return arbitroRepository.save(arbitro);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (arbitroRepository.existsById(id)) {
            arbitroRepository.deleteById(id);
        }
    }
}
