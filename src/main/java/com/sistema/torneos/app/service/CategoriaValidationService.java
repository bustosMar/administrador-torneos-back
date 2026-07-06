package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.*;
import com.sistema.torneos.app.domain.exception.CategoriaValidationException;
import com.sistema.torneos.app.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class CategoriaValidationService {

    @Autowired
    private JugadorEnCategoriaRepository jugadorEnCategoriaRepository;

    @Autowired
    private CategoriaTorneoRepository categoriaTorneoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Valida si un jugador puede inscribirse en una categoría específica
     * 
     * REGLAS DE NEGOCIO:
     * 1. Un jugador MÁXIMO 2 categorías activas por torneo
     * 2. Las combinaciones permitidas son:
     *    - Primera + Veteranos ✅
     *    - Segunda + Veteranos ✅
     *    - Primera + Segunda ❌ (NO PERMITIDO)
     * 3. Veteranos requiere edad mínima (41+)
     * 4. Primera y Segunda pueden tener sus propias restricciones de edad
     * 
     * @param jugador El jugador a validar
     * @param categoriaTorneo La categoría del torneo donde se desea inscribir
     * @throws CategoriaValidationException si la validación falla
     */
    public void validarInscripcionJugadorEnCategoria(Jugador jugador, CategoriaTorneo categoriaTorneo) {
        if (jugador == null || jugador.getFechaNacimiento() == null) {
            throw new CategoriaValidationException("El jugador no tiene fecha de nacimiento registrada");
        }

        if (categoriaTorneo == null || categoriaTorneo.getCategoria() == null) {
            throw new CategoriaValidationException("Categoría inválida");
        }

        Categoria categoria = categoriaTorneo.getCategoria();

        // Validación 1: ¿Está activa?
        if (!categoriaTorneo.isActiva() || !categoria.isActiva()) {
            throw new CategoriaValidationException(
                "La categoría '" + categoria.getNombre() + "' no está activa en este torneo"
            );
        }

        // Validación 2: Validar edad SOLO si tiene edadMinima o edadMaxima
        boolean tieneEdadMinima = categoria.getEdadMinima() != null;
        boolean tieneEdadMaxima = categoria.getEdadMaxima() != null;

        if (tieneEdadMinima || tieneEdadMaxima) {
            int edadJugador = calcularEdadJugador(jugador.getFechaNacimiento());

            if (tieneEdadMinima && edadJugador < categoria.getEdadMinima()) {
                throw new CategoriaValidationException(
                    "Jugador no puede jugar en la categoría '" + categoria.getNombre() + "'"
                );
            }
            if (tieneEdadMaxima && edadJugador > categoria.getEdadMaxima()) {
                throw new CategoriaValidationException(
                    "Jugador no puede jugar en la categoría '" + categoria.getNombre() + "'"
                );
            }
        }

        // Validación 3: Combinaciones permitidas
        validarCombinacionesCategorias(jugador, categoriaTorneo);
    }

    /**
     * Valida que la edad del jugador cumple con los requisitos de la categoría
     */
    private void validarEdadJugador(Jugador jugador, Categoria categoria) {
        int edadJugador = calcularEdadJugador(jugador.getFechaNacimiento());

        if (categoria.getEdadMinima() != null && edadJugador < categoria.getEdadMinima()) {
            throw new CategoriaValidationException(
                "Jugador no puede jugar en la categoría '" + categoria.getNombre() + "'"
            );
        }

        if (categoria.getEdadMaxima() != null && edadJugador > categoria.getEdadMaxima()) {
            throw new CategoriaValidationException(
                "Jugador no puede jugar en la categoría '" + categoria.getNombre() + "'"
            );
        }
    }

    /**
     * Valida combinaciones permitidas:
     * - Primera + Veteranos ✅
     * - Segunda + Veteranos ✅
     * - Primera + Segunda ❌
     * - Máximo 2 categorías por torneo
     */
    private void validarCombinacionesCategorias(Jugador jugador, CategoriaTorneo categoriaTorneo) {
        List<JugadorEnCategoria> inscripcionesActivas = jugadorEnCategoriaRepository
            .findByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(
                jugador.getId(),
                categoriaTorneo.getTorneo().getId()
            );

        Categoria categoriaNueva = categoriaTorneo.getCategoria();
        String nombreNueva = categoriaNueva.getNombre().toLowerCase();
        boolean nuevaEsVeterano = nombreNueva.contains("veteran");

        // Si ya tiene 2 categorías, no puede agregar más
        if (inscripcionesActivas.size() >= 2) {
            throw new CategoriaValidationException(
                "El jugador ya tiene 2 categorías en este torneo. Máximo permitido: 2 categorías."
            );
        }

        for (JugadorEnCategoria inscripcion : inscripcionesActivas) {
            String nombreExistente = inscripcion.getCategoriaTorneo().getCategoria().getNombre().toLowerCase();
            boolean existenteEsVeterano = nombreExistente.contains("veteran");

            // Si ya está en una categoría normal (no veterano) e intenta entrar a otra normal (no veterano)
            if (!nuevaEsVeterano && !existenteEsVeterano) {
                throw new CategoriaValidationException(
                    "El jugador ya está inscrito en '" + inscripcion.getCategoriaTorneo().getCategoria().getNombre() + "'. " +
                    "Solo puede agregar Veteranos como segunda categoría."
                );
            }

            // Si ya está en Veteranos e intenta entrar a otro Veteranos
            if (nuevaEsVeterano && existenteEsVeterano) {
                throw new CategoriaValidationException(
                    "El jugador ya está inscrito en Veteranos."
                );
            }
        }
    }

    /**
     * Calcula la edad actual de un jugador basado en su fecha de nacimiento
     */
    public int calcularEdadJugador(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Obtiene las categorías disponibles para un jugador en un torneo específico
     * Considera: máximo 2 categorías, no Primera+Segunda, validación de edad
     */
    public List<CategoriaTorneo> obtenerCategoriasDisponiblesParaJugador(Jugador jugador, Torneo torneo) {
        List<JugadorEnCategoria> inscripcionesActivas = jugadorEnCategoriaRepository
            .findByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(jugador.getId(), torneo.getId());

        String categoriaExistente = null;
        if (!inscripcionesActivas.isEmpty()) {
            categoriaExistente = inscripcionesActivas.get(0).getCategoriaTorneo().getCategoria().getNombre().toLowerCase();
        }

        final String categoriaEx = categoriaExistente;

        return categoriaTorneoRepository.findByTorneoAndActivaTrue(torneo).stream()
            .filter(ct -> {
                try {
                    // Si ya tiene 2 categorías, no mostrar más
                    if (inscripcionesActivas.size() >= 2) {
                        return false;
                    }

                    // Si ya está inscrito en esta categoría, no mostrar
                    if (jugadorEnCategoriaRepository.existsByJugadorIdAndCategoriaTorneoId(
                        jugador.getId(), ct.getId())) {
                        return false;
                    }

                    // Si ya tiene Primera, solo mostrar Veteranos
                    if (categoriaEx != null && categoriaEx.contains("primera")) {
                        String nombreActual = ct.getCategoria().getNombre().toLowerCase();
                        if (!nombreActual.contains("veteranos")) {
                            return false; // No mostrar Segunda
                        }
                    }

                    // Si ya tiene Segunda, solo mostrar Veteranos
                    if (categoriaEx != null && categoriaEx.contains("segunda")) {
                        String nombreActual = ct.getCategoria().getNombre().toLowerCase();
                        if (!nombreActual.contains("veteranos")) {
                            return false; // No mostrar Primera
                        }
                    }

                    // Si ya tiene Veteranos, puede mostrar Primera o Segunda (pero no ambas)
                    if (categoriaEx != null && categoriaEx.contains("veteranos")) {
                        String nombreActual = ct.getCategoria().getNombre().toLowerCase();
                        // OK, mostrar Primera o Segunda
                    }

                    // Validar edad
                    validarEdadJugador(jugador, ct.getCategoria());
                    return true;
                } catch (CategoriaValidationException e) {
                    return false; // No cumple requisitos
                }
            })
            .toList();
    }

    /**
     * Verifica si un jugador puede participar en una categoría específica
     */
    public boolean puedeParticiparEnCategoria(Jugador jugador, CategoriaTorneo categoriaTorneo) {
        try {
            validarInscripcionJugadorEnCategoria(jugador, categoriaTorneo);
            return true;
        } catch (CategoriaValidationException e) {
            return false;
        }
    }

    /**
     * Obtiene las categorías en las que está inscrito un jugador
     */
    public List<JugadorEnCategoria> obtenerCategoriasDelJugador(Jugador jugador) {
        return jugadorEnCategoriaRepository.findByJugadorAndActivoTrue(jugador);
    }
}
