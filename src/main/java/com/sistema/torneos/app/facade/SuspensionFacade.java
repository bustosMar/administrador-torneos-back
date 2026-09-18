package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Suspension;
import com.sistema.torneos.app.domain.repository.JugadorRepository;
import com.sistema.torneos.app.domain.repository.SuspensionRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.request.CrearSuspensionRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class SuspensionFacade {

    private final SuspensionRepository suspensionRepository;
    private final JugadorRepository jugadorRepository;

    @Autowired
    public SuspensionFacade(
            SuspensionRepository suspensionRepository,
            JugadorRepository jugadorRepository) {
        this.suspensionRepository = suspensionRepository;
        this.jugadorRepository = jugadorRepository;
    }

    public List<Suspension> findAll() {
        return suspensionRepository.findAll();
    }

    public Suspension findById(Long id) {
        return suspensionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Suspension create(Suspension suspension) {
        return suspensionRepository.save(suspension);
    }

    @Transactional
    public Suspension create(CrearSuspensionRequest request) {
        if (request.getJugadorId() == null) {
            throw new ResourceNotFoundException("Debe indicar el jugador de la suspensión.");
        }

        if (request.getFechaInicio() == null || request.getFechaFin() == null) {
            throw new ResourceNotFoundException("Las fechas de la suspensión son obligatorias.");
        }

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new ResourceNotFoundException("La fecha fin no puede ser anterior a la fecha inicio.");
        }

        Suspension suspension = new Suspension();
        suspension.setJugador(jugadorRepository.findById(request.getJugadorId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el jugador indicado.")));
        suspension.setFechaInicio(request.getFechaInicio());
        suspension.setFechaFin(request.getFechaFin());
        suspension.setMotivo(request.getMotivo());

        return suspensionRepository.save(suspension);
    }

    @Transactional
    public Suspension update(Long id, Suspension suspension) {
        Suspension suspensionExistente = suspensionRepository.findById(id).orElse(null);

        if (suspensionExistente == null) {
            return null;
        }

        suspensionExistente.setFechaInicio(suspension.getFechaInicio());
        suspensionExistente.setFechaFin(suspension.getFechaFin());
        suspensionExistente.setMotivo(suspension.getMotivo());

        return suspensionRepository.save(suspensionExistente);
    }

    @Transactional
    public void delete(Long id) {
        if (suspensionRepository.existsById(id)) {
            suspensionRepository.deleteById(id);
        }
    }
}
