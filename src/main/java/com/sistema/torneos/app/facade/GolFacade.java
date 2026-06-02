package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.domain.repository.GolRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class GolFacade {

    private final GolRepository golRepository;

    @Autowired
    public GolFacade(GolRepository golRepository) {
        this.golRepository = golRepository;
    }

    public List<Gol> findAll() {
        return golRepository.findAll();
    }

    public Gol findById(Long id) {
        return golRepository.findById(id).orElse(null);
    }

    @Transactional
    public Gol create(Gol gol) {
        return golRepository.save(gol);
    }

    @Transactional
    public Gol update(Long id, Gol gol) {
        if (golRepository.existsById(id)) {
            gol.setId(id);
            return golRepository.save(gol);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (golRepository.existsById(id)) {
            golRepository.deleteById(id);
        }
    }
}
