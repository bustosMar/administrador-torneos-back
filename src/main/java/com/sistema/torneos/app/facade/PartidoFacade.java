package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.PartidoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PartidoFacade {

    private final PartidoRepository partidoRepository;

    @Autowired
    public PartidoFacade(PartidoRepository partidoRepository) {
        this.partidoRepository = partidoRepository;
    }

    public List<Partido> findAll() {
        return partidoRepository.findAll();
    }

    public Partido findById(Long id) {
        return partidoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Partido create(Partido partido) {
        return partidoRepository.save(partido);
    }

    @Transactional
    public Partido update(Long id, Partido partido) {
        if (partidoRepository.existsById(id)) {
            partido.setId(id);
            return partidoRepository.save(partido);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (partidoRepository.existsById(id)) {
            partidoRepository.deleteById(id);
        }
    }
}
