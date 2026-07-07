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
  
    	// Guardar el JugadorEnEquipo
    	// NOTA: Permitimos múltiples JugadorEnEquipo del mismo jugador en el mismo torneo
    	// si son con diferentes categorías (Primera + Veteranos)
    	JugadorEnEquipo jugadorEnEquipoGuardado = jugadorEnEquipoRepository.save(
            JugadorEnEquipoMapper.INSTANCE.toEntity(jugadorEnEquipo)
        );

    	// Inscribir al jugador en la categoría del equipo
    	// AQUÍ se valida que cumpla todas las reglas (edad, combinaciones, etc.)
    	try {
    	    jugadorEnCategoriaService.inscribirJugadorEnCategoria(
    	        jugadorEnEquipo.getJugador(),
    	        equipoEnTorneo.getCategoriaTorneo().getId()
    	    );
    	} catch (CategoriaValidationException e) {
    	    // Si falla la validación, eliminar el JugadorEnEquipo guardado (ATOMIC)
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

        JugadorEnEquipoModel model =
                JugadorEnEquipoMapper.INSTANCE.toModel(entity);

        if (entity.getEquipo() == null || entity.getTorneo() == null) {
            return model;
        }

        // Si el model ya trae categoriaTorneo, buscar exactamente esa categoría
        if (model.getCategoriaTorneo() != null) {

            EquipoEnTorneo equipoEnTorneo =
                    equipoEnTorneoRepository
                            .findByEquipo_IdAndTorneo_IdAndCategoriaTorneo_Id(
                                    entity.getEquipo().getId(),
                                    entity.getTorneo().getId(),
                                    model.getCategoriaTorneo()
                            );

            if (equipoEnTorneo != null &&
                    equipoEnTorneo.getCategoriaTorneo() != null) {

                model.setCategoriaTorneo(
                        equipoEnTorneo.getCategoriaTorneo().getId()
                );

                model.setCategoriaTorneoNombre(
                        equipoEnTorneo.getCategoriaTorneo()
                                .getCategoria()
                                .getNombre()
                );
            }

            return model;
        }

        return model;
    }
}