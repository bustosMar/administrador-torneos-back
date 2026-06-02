package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.repository.JugadorRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class JugadorFacade {

    private final JugadorRepository jugadorRepository;

    @Autowired
    public JugadorFacade(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }

    public Jugador findById(Long id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    @Transactional
    public Jugador create(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    @Transactional
    public Jugador update(Long id, Jugador jugador) {
        if (jugadorRepository.existsById(id)) {
            jugador.setId(id);
            return jugadorRepository.save(jugador);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (jugadorRepository.existsById(id)) {
            jugadorRepository.deleteById(id);
        }
    }
}
