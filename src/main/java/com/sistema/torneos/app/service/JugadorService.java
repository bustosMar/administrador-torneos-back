package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Jugador;

import java.util.List;

public interface JugadorService {
    List<Jugador> findAll();
    Jugador findById(Long id);
    Jugador create(Jugador jugador);
    Jugador update(Long id, Jugador jugador);
    void delete(Long id);
}
