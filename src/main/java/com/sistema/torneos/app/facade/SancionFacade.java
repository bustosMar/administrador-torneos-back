package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.entity.Sancion;
import com.sistema.torneos.app.domain.entity.Suspension;
import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.domain.repository.JugadorRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.domain.repository.SancionRepository;
import com.sistema.torneos.app.domain.repository.SuspensionRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.SancionModel;
import com.sistema.torneos.app.web.model.mapper.SancionMapper;
import com.sistema.torneos.app.web.model.mapper.TorneoMapper;
import com.sistema.torneos.app.service.EvaluacionFechasSuspension;
import com.sistema.torneos.app.service.GeminiSuspensionEvaluator;

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
    private final SuspensionRepository suspensionRepository;
    private final GeminiSuspensionEvaluator suspensionEvaluator;

    @Autowired
    public SancionFacade(
            SancionRepository sancionRepository,
            PartidoRepository partidoRepository,
            JugadorRepository jugadorRepository,
            EquipoEnTorneoRepository equipoEnTorneoRepository,
            SuspensionRepository suspensionRepository,
            GeminiSuspensionEvaluator suspensionEvaluator) {

        this.sancionRepository = sancionRepository;
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
        this.equipoEnTorneoRepository = equipoEnTorneoRepository;
        this.suspensionRepository = suspensionRepository;
        this.suspensionEvaluator = suspensionEvaluator;
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
        String tipo = normalizarTipo(sancionModel.getTipo());
        Sancion sancion = SancionMapper.INSTANCE.toEntity(sancionModel);
        sancion.setJugador(
        	    jugadorRepository.findById(sancionModel.getJugador()).get()
        	);

        	sancion.setEquipoTorneo(
        			equipoEnTorneoRepository.findById(sancionModel.getEquipoTorneo()).get()
        	);

        	sancion.setPartido(
        	    partidoRepository.findById(sancionModel.getPartido()).get()
        	);
        sancion.setTipo(tipo);

        int amarillasPrevias = "ROJA".equals(tipo)
                ? Math.toIntExact(sancionRepository.countByPartidoIdAndJugadorIdAndTipoIgnoreCase(
                        sancionModel.getPartido(), sancionModel.getJugador(), "AMARILLA"))
                : 0;

        Sancion sancionGuardada = sancionRepository.save(sancion);
        boolean requiereSuspension = "ROJA".equals(tipo) && amarillasPrevias < 2;
        boolean suspensionGenerada = false;
        boolean suspensionPendienteRevision = false;
        String mensajeSuspension = null;

        if (requiereSuspension) {
            try {
                EvaluacionFechasSuspension evaluacion = suspensionEvaluator.evaluar(
                        sancionGuardada.getPartido(), sancionGuardada.getJugador(), amarillasPrevias,sancionGuardada.getObservacion());

                Suspension suspension = new Suspension();
                suspension.setJugador(sancionGuardada.getJugador());
                suspension.setFechaInicio(evaluacion.fechaInicio());
                suspension.setFechaFin(evaluacion.fechaFin());
                suspension.setMotivo(evaluacion.motivo());
                suspensionRepository.save(suspension);
                suspensionGenerada = true;
                mensajeSuspension = "Suspensión evaluada y creada por IA.";
            } catch (IllegalStateException e) {
                Suspension suspension = new Suspension();
                suspension.setJugador(sancionGuardada.getJugador());
                suspension.setMotivo(sancionGuardada.getObservacion());
                suspension.setFechaInicio(sancionGuardada.getPartido().getFecha());
                suspension.setFechaFin(sancionGuardada.getPartido().getFecha());
                suspensionRepository.save(suspension);
                suspensionPendienteRevision = true;
                mensajeSuspension = "La roja fue registrada, pero la suspensión requiere revisión manual: "
                        + e.getMessage();
            }
        }

        SancionModel resultado = SancionMapper.INSTANCE.toModel(sancionGuardada);
        resultado.setAmarillasPrevias(amarillasPrevias);
        resultado.setSuspensionGenerada(suspensionGenerada);
        resultado.setSuspensionPendienteRevision(suspensionPendienteRevision);
        resultado.setMensajeSuspension(mensajeSuspension);
        return resultado;
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
