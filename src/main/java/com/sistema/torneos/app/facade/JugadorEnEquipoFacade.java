package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.JugadorEnEquipoRepository;
import com.sistema.torneos.app.domain.repository.CategoriaTorneoRepository;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.service.JugadorEnCategoriaService;
import com.sistema.torneos.app.web.model.JugadorEnEquipoModel;
import com.sistema.torneos.app.web.model.mapper.JugadorEnEquipoMapper;

import com.sistema.torneos.app.domain.exception.CategoriaValidationException;
import com.sistema.torneos.app.domain.repository.JugadorEnCategoriaRepository;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class JugadorEnEquipoFacade {

    private final JugadorEnEquipoRepository jugadorEnEquipoRepository;
    
    @Autowired
    private EquipoEnTorneoRepository equipoEnTorneoRepository;

    @Autowired
    private CategoriaTorneoRepository categoriaTorneoRepository;
    
    @Autowired
    private JugadorEnCategoriaRepository jugadorEnCategoriaRepository;
    
    @Autowired
    private JugadorEnCategoriaService jugadorEnCategoriaService;

    @Autowired
    public JugadorEnEquipoFacade(JugadorEnEquipoRepository jugadorEnEquipoRepository) {
        this.jugadorEnEquipoRepository = jugadorEnEquipoRepository;
    }

    public List<JugadorEnEquipoModel> findAll() {

        List<JugadorEnEquipo> jugadores = jugadorEnEquipoRepository.findAll();

        return jugadores.stream()
            .map(this::enriquecerConCategoria)
            .toList();
    }

    public List<JugadorEnEquipoModel> findAllByTorneoActivo() {

        List<JugadorEnEquipo> jugadores = jugadorEnEquipoRepository.findAllByTorneoActivo();

        return jugadores.stream()
            .map(this::enriquecerConCategoria)
            .toList();
    }

    public JugadorEnEquipoModel findById(Long id) {
        JugadorEnEquipo jugador = jugadorEnEquipoRepository.findById(id).orElse(null);
        return jugador != null ? enriquecerConCategoria(jugador) : null;
    }

    @Transactional
    public JugadorEnEquipoModel create(JugadorEnEquipoModel jugadorEnEquipo) {
    	
    	// Obtener el EquipoEnTorneo para conocer la categoría del equipo
    	// Usamos la categoría que viene del frontend (categoriaTorneo en el model)
    	EquipoEnTorneo equipoEnTorneo = equipoEnTorneoRepository.findByEquipo_IdAndTorneo_IdAndCategoriaTorneo_Id(
    	    jugadorEnEquipo.getEquipo(), 
    	    jugadorEnEquipo.getTorneo(),
    	    jugadorEnEquipo.getCategoriaTorneo()
    	);

    	if (equipoEnTorneo == null || equipoEnTorneo.getCategoriaTorneo() == null) {
    	    throw new RuntimeException("El equipo no tiene una categoría asignada en este torneo");
    	}
    	
    	// Validar que no esté activo en la categoría específica
    	if (jugadorEnCategoriaRepository.existsByJugadorIdAndCategoriaTorneoId(
    	    jugadorEnEquipo.getJugador(),
    	    equipoEnTorneo.getCategoriaTorneo().getId()
    	)) {
    	    throw new RuntimeException("Jugador ya está inscrito en esta categoría");
    	}
  
    	// Guardar el JugadorEnEquipo
    	JugadorEnEquipo jugadorEnEquipoGuardado = jugadorEnEquipoRepository.save(
            JugadorEnEquipoMapper.INSTANCE.toEntity(jugadorEnEquipo)
        );

    	// Inscribir al jugador en la categoría del equipo
    	try {
    	    jugadorEnCategoriaService.inscribirJugadorEnCategoria(
    	        jugadorEnEquipo.getJugador(),
    	        equipoEnTorneo.getCategoriaTorneo().getId()
    	    );
    	} catch (CategoriaValidationException e) {
    	    jugadorEnEquipoRepository.delete(jugadorEnEquipoGuardado);
    	    throw new RuntimeException(e.getMessage());
    	}

        return enriquecerConCategoria(jugadorEnEquipoGuardado);
    }

    @Transactional
    public JugadorEnEquipoModel update(Long id, JugadorEnEquipoModel jugadorEnEquipo) {
        if (jugadorEnEquipoRepository.existsById(id)) {

            JugadorEnEquipo entity = JugadorEnEquipoMapper.INSTANCE.toEntity(jugadorEnEquipo);
            entity.setId(id);

            return enriquecerConCategoria(
                    jugadorEnEquipoRepository.save(entity)
            );
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (jugadorEnEquipoRepository.existsById(id)) {
            jugadorEnEquipoRepository.deleteById(id);
        }
    }
    
    /**
     * Enriquece el modelo con información de categoría del EquipoEnTorneo
     */
    private JugadorEnEquipoModel enriquecerConCategoria(JugadorEnEquipo entity) {
        JugadorEnEquipoModel model = JugadorEnEquipoMapper.INSTANCE.toModel(entity);
        
        // Obtener la categoría desde EquipoEnTorneo
        if (entity.getEquipo() != null && entity.getTorneo() != null) {
            List<EquipoEnTorneo> equiposEnTorneo = equipoEnTorneoRepository.findByEquipo_IdAndTorneo_Id(
                entity.getEquipo().getId(),
                entity.getTorneo().getId()
            );
            
            // Si hay registros, usar el primero (en caso de múltiples categorías)
            if (equiposEnTorneo != null && !equiposEnTorneo.isEmpty()) {
                EquipoEnTorneo equipoEnTorneo = equiposEnTorneo.get(0);
                if (equipoEnTorneo.getCategoriaTorneo() != null) {
                    model.setCategoriaTorneo(equipoEnTorneo.getCategoriaTorneo().getId());
                    model.setCategoriaTorneoNombre(equipoEnTorneo.getCategoriaTorneo().getCategoria().getNombre());
                }
            }
        }
        
        return model;
    }
}