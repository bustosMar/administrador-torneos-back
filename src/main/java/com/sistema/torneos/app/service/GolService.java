package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Gol;

import java.util.List;

public interface GolService {
    List<Gol> findAll();
    Gol findById(Long id);
    Gol create(Gol gol);
    Gol update(Long id, Gol gol);
    void delete(Long id);
}
