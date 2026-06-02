package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class EquipoEnTorneoFacade {

    private final EquipoEnTorneoRepository equipoEnTorneoRepository;

    @Autowired
    public EquipoEnTorneoFacade(EquipoEnTorneoRepository equipoEnTorneoRepository) {
        this.equipoEnTorneoRepository = equipoEnTorneoRepository;
    }

    public List<EquipoEnTorneo> findAll() {
        return equipoEnTorneoRepository.findAll();
    }

    public EquipoEnTorneo findById(Long id) {
        return equipoEnTorneoRepository.findById(id).orElse(null);
    }

    @Transactional
    public EquipoEnTorneo create(EquipoEnTorneo equipoEnTorneo) {
        return equipoEnTorneoRepository.save(equipoEnTorneo);
    }

    @Transactional
    public EquipoEnTorneo update(Long id, EquipoEnTorneo equipoEnTorneo) {
        if (equipoEnTorneoRepository.existsById(id)) {
            equipoEnTorneo.setId(id);
            return equipoEnTorneoRepository.save(equipoEnTorneo);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (equipoEnTorneoRepository.existsById(id)) {
            equipoEnTorneoRepository.deleteById(id);
        }
    }
}
