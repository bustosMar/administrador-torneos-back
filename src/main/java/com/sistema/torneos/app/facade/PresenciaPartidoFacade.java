package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import com.sistema.torneos.app.domain.repository.PresenciaPartidoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PresenciaPartidoFacade {

    private final PresenciaPartidoRepository presenciaPartidoRepository;

    @Autowired
    public PresenciaPartidoFacade(PresenciaPartidoRepository presenciaPartidoRepository) {
        this.presenciaPartidoRepository = presenciaPartidoRepository;
    }

    public List<PresenciaPartido> findAll() {
        return presenciaPartidoRepository.findAll();
    }

    public PresenciaPartido findById(Long id) {
        return presenciaPartidoRepository.findById(id).orElse(null);
    }

    @Transactional
    public PresenciaPartido create(PresenciaPartido presenciaPartido) {
        return presenciaPartidoRepository.save(presenciaPartido);
    }

    @Transactional
    public PresenciaPartido update(Long id, PresenciaPartido presenciaPartido) {
        if (presenciaPartidoRepository.existsById(id)) {
            presenciaPartido.setId(id);
            return presenciaPartidoRepository.save(presenciaPartido);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (presenciaPartidoRepository.existsById(id)) {
            presenciaPartidoRepository.deleteById(id);
        }
    }
}
