package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.entity.Sancion;
import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.domain.repository.JugadorRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.domain.repository.SancionRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.SancionModel;
import com.sistema.torneos.app.web.model.mapper.SancionMapper;
import com.sistema.torneos.app.web.model.mapper.TorneoMapper;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class SancionFacade {

    private final SancionRepository sancionRepository;
    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;
    private final EquipoEnTorneoRepository equipoEnTorneoRepository;

    @Autowired
    public SancionFacade(
            SancionRepository sancionRepository,
            PartidoRepository partidoRepository,
            JugadorRepository jugadorRepository,
            EquipoEnTorneoRepository equipoEnTorneoRepository) {

        this.sancionRepository = sancionRepository;
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
        this.equipoEnTorneoRepository = equipoEnTorneoRepository;
    }

    public List<SancionModel> findAll() {
    	
    	List<Sancion> sancion = sancionRepository.findAll();
        return SancionMapper.INSTANCE.toModel(sancion);
    }

    public SancionModel findById(Long id) {
    	
    	return SancionMapper.INSTANCE.toModel(sancionRepository.findById(id).orElse(null));
   
    }

    @Transactional
    public SancionModel create(SancionModel sancionModel) {
        
    	normalizarTipo(sancionModel.getTipo());
    	
        return SancionMapper.INSTANCE.toModel(sancionRepository.save(SancionMapper.INSTANCE.toEntity(sancionModel)));
    }

    @Transactional
    public SancionModel update(Long id, SancionModel sancionModel) {
        if (sancionRepository.existsById(id)) {
            Sancion sancion = SancionMapper.INSTANCE.toEntity(sancionModel);
            sancion.setId(id);
            sancion.setTipo(normalizarTipo(sancionModel.getTipo()));
            return SancionMapper.INSTANCE.toModel(sancionRepository.save(sancion));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (sancionRepository.existsById(id)) {
            sancionRepository.deleteById(id);
        }
    }
 
   
    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new RuntimeException("El tipo de sanción es obligatorio y debe ser ROJA o AMARILLA.");
        }

        String tipoNormalizado = tipo.trim().toUpperCase(Locale.ROOT);

        if (!"ROJA".equals(tipoNormalizado) && !"AMARILLA".equals(tipoNormalizado)) {
            throw new RuntimeException("Tipo de sanción inválido. Solo se permite ROJA o AMARILLA.");
        }

        return tipoNormalizado;
    }
}
