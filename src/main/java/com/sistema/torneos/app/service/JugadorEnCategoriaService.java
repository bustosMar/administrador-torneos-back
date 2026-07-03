package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.JugadorEnCategoria;
import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.exception.CategoriaValidationException;
import com.sistema.torneos.app.domain.repository.JugadorEnCategoriaRepository;
import com.sistema.torneos.app.domain.repository.CategoriaTorneoRepository;
import com.sistema.torneos.app.domain.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JugadorEnCategoriaService {

    @Autowired
    private JugadorEnCategoriaRepository jugadorEnCategoriaRepository;

    @Autowired
    private CategoriaTorneoRepository categoriaTorneoRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private CategoriaValidationService categoriaValidationService;

    /**
     * Inscribe un jugador en una categoría de un torneo
     * Valida automáticamente TODAS las reglas de negocio
     */
    public JugadorEnCategoria inscribirJugadorEnCategoria(Long jugadorId, Long categoriaTorneoId) {
        Optional<Jugador> jugador = jugadorRepository.findById(jugadorId);
        Optional<CategoriaTorneo> categoriaTorneo = categoriaTorneoRepository.findById(categoriaTorneoId);

        if (jugador.isEmpty()) {
            throw new CategoriaValidationException("Jugador no encontrado");
        }
        if (categoriaTorneo.isEmpty()) {
            throw new CategoriaValidationException("Categoría del torneo no encontrada");
        }

        // Validar que cumpla los requisitos (REGLA DE NEGOCIO)
        categoriaValidationService.validarInscripcionJugadorEnCategoria(jugador.get(), categoriaTorneo.get());

        // Crear la inscripción
        JugadorEnCategoria jugadorEnCategoria = new JugadorEnCategoria();
        jugadorEnCategoria.setJugador(jugador.get());
        jugadorEnCategoria.setCategoriaTorneo(categoriaTorneo.get());
        jugadorEnCategoria.setFechaInscripcion(LocalDate.now());
        jugadorEnCategoria.setActivo(true);

        return jugadorEnCategoriaRepository.save(jugadorEnCategoria);
    }

    /**
     * Obtiene todos los jugadores inscritos en una categoría de torneo
     */
    public List<JugadorEnCategoria> obtenerJugadoresEnCategoria(Long categoriaTorneoId) {
        Optional<CategoriaTorneo> categoriaTorneo = categoriaTorneoRepository.findById(categoriaTorneoId);
        if (categoriaTorneo.isPresent()) {
            return jugadorEnCategoriaRepository.findByCategoriaTorneoAndActivoTrue(categoriaTorneo.get());
        }
        return List.of();
    }

    /**
     * Obtiene las categorías en las que está inscrito un jugador
     */
    public List<JugadorEnCategoria> obtenerCategoriasDelJugador(Long jugadorId) {
        Optional<Jugador> jugador = jugadorRepository.findById(jugadorId);
        if (jugador.isPresent()) {
            return jugadorEnCategoriaRepository.findByJugadorAndActivoTrue(jugador.get());
        }
        return List.of();
    }

    /**
     * Obtiene las inscripciones activas de un jugador en un torneo específico
     */
    public List<JugadorEnCategoria> obtenerInscripcionesDelJugadorEnTorneo(Long jugadorId, Long torneoId) {
        return jugadorEnCategoriaRepository.findByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(jugadorId, torneoId);
    }

    /**
     * Desactiva la inscripción de un jugador en una categoría
     */
    public void desactivarInscripcion(Long id) {
        Optional<JugadorEnCategoria> jugadorEnCategoria = jugadorEnCategoriaRepository.findById(id);
        if (jugadorEnCategoria.isPresent()) {
            JugadorEnCategoria jec = jugadorEnCategoria.get();
            jec.setActivo(false);
            jugadorEnCategoriaRepository.save(jec);
        }
    }

    /**
     * Elimina la inscripción de un jugador en una categoría
     */
    public void eliminarInscripcion(Long id) {
        jugadorEnCategoriaRepository.deleteById(id);
    }

    /**
     * Verifica si un jugador está inscrito en una categoría específica
     */
    public boolean estaInscritoEnCategoria(Long jugadorId, Long categoriaTorneoId) {
        return jugadorEnCategoriaRepository.existsByJugadorIdAndCategoriaTorneoId(jugadorId, categoriaTorneoId);
    }

    /**
     * Obtiene la inscripción específica de un jugador en una categoría
     */
    public Optional<JugadorEnCategoria> obtenerInscripcion(Long jugadorId, Long categoriaTorneoId) {
        return jugadorEnCategoriaRepository.findByJugadorIdAndCategoriaTorneoId(jugadorId, categoriaTorneoId);
    }

    /**
     * Obtiene el conteo de inscripciones activas de un jugador en un torneo
     */
    public long contarInscripcionesActivas(Long jugadorId, Long torneoId) {
        return jugadorEnCategoriaRepository.countByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(jugadorId, torneoId);
    }
}
