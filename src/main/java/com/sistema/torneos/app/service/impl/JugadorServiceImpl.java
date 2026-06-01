package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.repository.JugadorRepository;
import com.sistema.torneos.app.service.JugadorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JugadorServiceImpl implements JugadorService {

   private JugadorRepository repository;
    
    
    public JugadorServiceImpl(JugadorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Jugador> findAll() {
        return (List<Jugador>) this.repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Jugador findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Jugador create(Jugador jugador) {
        return this.repository.save(jugador);
    }

    @Override
    @Transactional
    public Jugador update(Long id, Jugador jugador) {
        Jugador existingJugador = this.findById(id);
        if (existingJugador != null) {
            // Update the properties of existingJugador with those from jugador
            return this.repository.save(existingJugador);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

}
