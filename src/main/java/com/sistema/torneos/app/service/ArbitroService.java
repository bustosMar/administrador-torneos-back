package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Arbitro;

import java.util.List;

public interface ArbitroService {
    List<Arbitro> findAll();
    Arbitro findById(Long id);
    Arbitro create(Arbitro arbitro);
    Arbitro update(Long id, Arbitro arbitro);
    void delete(Long id);
}
