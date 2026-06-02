package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.domain.repository.TorneoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class TorneoFacade {

    private final TorneoRepository torneoRepository;

    @Autowired
    public TorneoFacade(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public List<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Torneo create(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    @Transactional
    public Torneo update(Long id, Torneo torneo) {
        if (torneoRepository.existsById(id)) {
            torneo.setId(id);
            return torneoRepository.save(torneo);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (torneoRepository.existsById(id)) {
            torneoRepository.deleteById(id);
        }
    }
}
