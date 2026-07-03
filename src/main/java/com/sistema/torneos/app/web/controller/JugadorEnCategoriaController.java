package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.JugadorEnCategoria;
import com.sistema.torneos.app.domain.exception.CategoriaValidationException;
import com.sistema.torneos.app.service.JugadorEnCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jugador-categoria")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class JugadorEnCategoriaController {

    @Autowired
    private JugadorEnCategoriaService jugadorEnCategoriaService;

    /**
     * ⭐ ENDPOINT PRINCIPAL: Inscribe un jugador en una categoría de un torneo
     * VALIDA AUTOMÁTICAMENTE:
     * ✓ Categoría permite inscripción (Solo Veteranos)
     * ✓ Edad del jugador cumple requisitos
     * ✓ No está inscrito en otra categoría del mismo torneo
     * 
     * POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=1
     */
    @PostMapping("/inscribir")
    public ResponseEntity<?> inscribirJugadorEnCategoria(
            @RequestParam Long jugadorId,
            @RequestParam Long categoriaTorneoId) {
        try {
            JugadorEnCategoria inscripcion = jugadorEnCategoriaService
                    .inscribirJugadorEnCategoria(jugadorId, categoriaTorneoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
        } catch (CategoriaValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al inscribir jugador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Obtiene todos los jugadores inscritos en una categoría de torneo
     */
    @GetMapping("/categoria/{categoriaTorneoId}")
    public ResponseEntity<List<JugadorEnCategoria>> obtenerJugadoresEnCategoria(
            @PathVariable Long categoriaTorneoId) {
        List<JugadorEnCategoria> jugadores = jugadorEnCategoriaService
                .obtenerJugadoresEnCategoria(categoriaTorneoId);
        return ResponseEntity.ok(jugadores);
    }

    /**
     * Obtiene las categorías en las que está inscrito un jugador
     */
    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<JugadorEnCategoria>> obtenerCategoriasDelJugador(
            @PathVariable Long jugadorId) {
        List<JugadorEnCategoria> categorias = jugadorEnCategoriaService
                .obtenerCategoriasDelJugador(jugadorId);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtiene las inscripciones activas de un jugador en un torneo específico
     */
    @GetMapping("/jugador/{jugadorId}/torneo/{torneoId}")
    public ResponseEntity<List<JugadorEnCategoria>> obtenerInscripcionesDelJugadorEnTorneo(
            @PathVariable Long jugadorId,
            @PathVariable Long torneoId) {
        List<JugadorEnCategoria> inscripciones = jugadorEnCategoriaService
                .obtenerInscripcionesDelJugadorEnTorneo(jugadorId, torneoId);
        return ResponseEntity.ok(inscripciones);
    }

    /**
     * Verifica si un jugador está inscrito en una categoría
     */
    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Boolean>> verificarInscripcion(
            @RequestParam Long jugadorId,
            @RequestParam Long categoriaTorneoId) {
        boolean estaInscrito = jugadorEnCategoriaService
                .estaInscritoEnCategoria(jugadorId, categoriaTorneoId);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("inscrito", estaInscrito);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Obtiene la inscripción específica de un jugador en una categoría
     */
    @GetMapping("/inscripcion")
    public ResponseEntity<JugadorEnCategoria> obtenerInscripcion(
            @RequestParam Long jugadorId,
            @RequestParam Long categoriaTorneoId) {
        Optional<JugadorEnCategoria> inscripcion = jugadorEnCategoriaService
                .obtenerInscripcion(jugadorId, categoriaTorneoId);
        return inscripcion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Desactiva la inscripción de un jugador en una categoría
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarInscripcion(@PathVariable Long id) {
        jugadorEnCategoriaService.desactivarInscripcion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina la inscripción de un jugador en una categoría
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        jugadorEnCategoriaService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
