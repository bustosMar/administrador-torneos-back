package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.web.model.EquipoEnTorneoModel;
import com.sistema.torneos.app.web.model.mapper.EquipoEnTorneoMapper;
import com.sistema.torneos.app.web.model.mapper.EquipoEnTorneoResponseMapper;
import com.sistema.torneos.app.web.model.response.EquipoEnTorneoResponse;

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

    public List<EquipoEnTorneoModel> findAll() {
    	
    	 List<EquipoEnTorneo> equipos = equipoEnTorneoRepository.findAll();

         return EquipoEnTorneoMapper.INSTANCE.toModel(equipos);
    }

    public EquipoEnTorneoModel findById(Long id) {
    	 return EquipoEnTorneoMapper.INSTANCE.toModel(equipoEnTorneoRepository.findById(id).orElse(null));
    }

    @Transactional
    public EquipoEnTorneoModel create(EquipoEnTorneoModel equipoEnTorneo) {
    	
    	 EquipoEnTorneo equipo = null;
    	 
    	 equipo = equipoEnTorneoRepository.findByEquipo_IdAndTorneo_Id(equipoEnTorneo.getEquipo(), equipoEnTorneo.getTorneo());
    	
    	 if (equipo!=null) {
    	 	 throw new RuntimeException("Ya existe un equipo con ese nombre en este torneo");
    	 }
    	
    	 return EquipoEnTorneoMapper.INSTANCE.toModel(equipoEnTorneoRepository.save(EquipoEnTorneoMapper.INSTANCE.toEntity(equipoEnTorneo)));
    }

    @Transactional
    public EquipoEnTorneoModel update(Long id, EquipoEnTorneoModel equipoEnTorneo) {
        if (equipoEnTorneoRepository.existsById(id)) {
        	
        	 EquipoEnTorneo equipoC = null;
        	 
        	 equipoC = equipoEnTorneoRepository.findByEquipo_IdAndTorneo_IdAndGrupo_Id(equipoEnTorneo.getEquipo(), equipoEnTorneo.getTorneo(),equipoEnTorneo.getGrupo());
        	
        	 if (equipoC!=null) {
        	 	 throw new RuntimeException("Ya existe un equipo con ese nombre en este torneo y en este grupo");
        	 }
        	
        	EquipoEnTorneo equipo = EquipoEnTorneoMapper.INSTANCE.toEntity(equipoEnTorneo);
        	equipo.setId(id);
        	return EquipoEnTorneoMapper.INSTANCE.toModel(equipoEnTorneoRepository.save(equipo));
        
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
