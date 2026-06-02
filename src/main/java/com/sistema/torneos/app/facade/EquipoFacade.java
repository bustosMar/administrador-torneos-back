package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Equipo;
import com.sistema.torneos.app.domain.repository.EquipoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class EquipoFacade {

    private final EquipoRepository equipoRepository;

    @Autowired
    public EquipoFacade(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> findAll() {
        return equipoRepository.findAll();
    }

    public Equipo findById(Long id) {
        return equipoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Equipo create(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Transactional
    public Equipo update(Long id, Equipo equipo) {
        if (equipoRepository.existsById(id)) {
            equipo.setId(id);
            return equipoRepository.save(equipo);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (equipoRepository.existsById(id)) {
            equipoRepository.deleteById(id);
        }
    }
}
