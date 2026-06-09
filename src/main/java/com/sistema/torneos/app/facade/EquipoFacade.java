package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Equipo;
import com.sistema.torneos.app.web.model.EquipoModel;
import com.sistema.torneos.app.domain.repository.EquipoRepository;
import com.sistema.torneos.app.web.model.mapper.EquipoMapper;

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

    public List<EquipoModel> findAll() {
    	List<Equipo> equipo = equipoRepository.findAll();
        return EquipoMapper.INSTANCE.toModel(equipo);
    }

    public EquipoModel findById(Long id) {
        return EquipoMapper.INSTANCE.toModel(equipoRepository.findById(id).orElse(null));
    }

    @Transactional
    public EquipoModel create(EquipoModel equipoModel) {
    	
    	Equipo equipo = null;
    	
    	equipo = equipoRepository.findByNombre(equipoModel.getNombre());
    	
    	if (equipo!=null) {
    	    throw new RuntimeException("Ya existe un equipo con ese nombre");
    	}
    	
    	return EquipoMapper.INSTANCE.toModel(equipoRepository.save(EquipoMapper.INSTANCE.toEntity(equipoModel)));

    }

    @Transactional
    public EquipoModel update(Long id, EquipoModel equipoModel) {
        if (equipoRepository.existsById(id)) {
        	
        	Equipo equipo = EquipoMapper.INSTANCE.toEntity(equipoModel);
        	equipo.setId(id);
            return EquipoMapper.INSTANCE.toModel(equipoRepository.save(equipo));
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
