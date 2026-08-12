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
import com.sistema.torneos.app.web.model.SuspensionModel;
import com.sistema.torneos.app.web.model.mapper.SancionMapper;
import com.sistema.torneos.app.web.model.mapper.TorneoMapper;
import com.sistema.torneos.app.service.EvaluacionFechasSuspension;
import com.sistema.torneos.app.service.GeminiSuspensionEvaluator;
import com.sistema.torneos.app.web.model.response.JugadorSancionesSuspensionesResponse;
import com.sistema.torneos.app.web.model.response.JugadoresSancionesSuspensionesResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
    
    public List<Jugador> buscarJugadores(String texto) {

        // =====================================================
        // VALIDAR
        // =====================================================

        if (
                texto == null ||
                texto.trim().isEmpty()
        ) {

            return List.of();
        }


        // =====================================================
        // LIMPIAR TEXTO
        // =====================================================

        String busqueda =
                texto.trim();


        // =====================================================
        // SEPARAR PALABRAS
        // =====================================================

        String[] partes =
                busqueda.split("\\s+");


        // =====================================================
        // UNA SOLA PALABRA
        //
        // Ejemplo:
        //
        // Mar
        //
        // Busca en:
        //
        // nombre
        // OR
        // apellido
        // =====================================================

        if (partes.length == 1) {

            String parte =
                    partes[0];


            return jugadorRepository
                    .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                            parte,
                            parte
                    );
        }


        // =====================================================
        // DOS O MÁS PALABRAS
        //
        // Nos quedamos con las primeras dos.
        //
        // Ejemplo:
        //
        // Mar Bus
        // =====================================================

        String parte1 =
                partes[0];

        String parte2 =
                partes[1];


        // =====================================================
        // BÚSQUEDA:
        //
        // nombre = Mar
        // apellido = Bus
        // =====================================================

        List<Jugador> resultadoNormal =
                jugadorRepository
                        .findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCase(
                                parte1,
                                parte2
                        );


        // =====================================================
        // BÚSQUEDA INVERSA:
        //
        // apellido = Mar
        // nombre = Bus
        //
        // Esto permite:
        //
        // Bus Mar
        // =====================================================

        List<Jugador> resultadoInverso =
                jugadorRepository
                        .findByApellidoContainingIgnoreCaseAndNombreContainingIgnoreCase(
                                parte1,
                                parte2
                        );


        // =====================================================
        // ELIMINAR DUPLICADOS
        //
        // LinkedHashMap conserva el orden.
        // =====================================================

        Map<Long, Jugador> resultados =
                new LinkedHashMap<>();


        // =====================================================
        // AGREGAR RESULTADOS NORMALES
        // =====================================================

        for (
                Jugador jugador :
                resultadoNormal
        ) {

            if (jugador != null) {

                resultados.put(
                        jugador.getId(),
                        jugador
                );
            }
        }


        // =====================================================
        // AGREGAR RESULTADOS INVERSOS
        // =====================================================

        for (
                Jugador jugador :
                resultadoInverso
        ) {

            if (jugador != null) {

                resultados.put(
                        jugador.getId(),
                        jugador
                );
            }
        }


        // =====================================================
        // RETORNAR
        // =====================================================

        return new ArrayList<>(
                resultados.values()
        );
    }


    // =========================================================
    // BUSCAR SANCIONES Y SUSPENSIONES POR JUGADOR
    // =========================================================

    public JugadoresSancionesSuspensionesResponse
    buscarSancionesSuspensionesPorJugador(
            Long jugadorId
    ) {

        // =====================================================
        // VALIDAR ID
        // =====================================================

        if (jugadorId == null) {

            throw new IllegalArgumentException(
                    "El jugadorId es obligatorio."
            );
        }


        // =====================================================
        // BUSCAR JUGADOR
        // =====================================================

        Jugador jugador =
                jugadorRepository
                        .findById(jugadorId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "No se encontró el jugador con id: "
                                                + jugadorId
                                )
                        );


        // =====================================================
        // BUSCAR SANCIONES
        // =====================================================

        List<Sancion> sancionesEntity =
                sancionRepository
                        .findByJugadorId(jugadorId);


        if (sancionesEntity == null) {

            sancionesEntity =
                    new ArrayList<>();
        }


        // =====================================================
        // CONVERTIR SANCIONES
        // =====================================================

        List<SancionModel> sanciones =
                sancionesEntity
                        .stream()
                        .map(
                                this::convertirSancionModel
                        )
                        .toList();


        // =====================================================
        // BUSCAR SUSPENSIONES
        // =====================================================

        List<Suspension> suspensionesEntity =
                suspensionRepository
                        .findByJugadorId(jugadorId);


        if (suspensionesEntity == null) {

            suspensionesEntity =
                    new ArrayList<>();
        }


        // =====================================================
        // CONVERTIR SUSPENSIONES
        // =====================================================

        List<SuspensionModel> suspensiones =
                suspensionesEntity
                        .stream()
                        .map(
                                this::convertirSuspensionModel
                        )
                        .toList();


        // =====================================================
        // EQUIPO
        //
        // Una sanción tiene:
        //
        // Sancion
        //    |
        //    +-- EquipoEnTorneo
        //
        // Tomamos el primer equipo encontrado.
        // =====================================================

        Long equipoId = null;

        String nombreEquipo = null;


        for (
                Sancion sancion :
                sancionesEntity
        ) {

            if (
                    sancion != null &&
                    sancion.getEquipoTorneo() != null
            ) {

                EquipoEnTorneo equipoTorneo =
                        sancion.getEquipoTorneo();


                equipoId =
                        equipoTorneo.getId();


                if (
                        equipoTorneo.getEquipo() != null
                ) {

                    nombreEquipo =
                            equipoTorneo
                                    .getEquipo()
                                    .getNombre();
                }


                break;
            }
        }


        // =====================================================
        // CREAR RESPUESTA DEL JUGADOR
        // =====================================================

        JugadorSancionesSuspensionesResponse
                jugadorResponse =

                JugadorSancionesSuspensionesResponse
                        .builder()

                        .jugadorId(
                                jugador.getId()
                        )

                        .jugador(
                                construirNombreJugador(
                                        jugador
                                )
                        )

                        .equipoId(
                                equipoId
                        )

                        .equipo(
                                nombreEquipo
                        )

                        .sanciones(
                                sanciones
                        )

                        .suspensiones(
                                suspensiones
                        )

                        .build();


        // =====================================================
        // RESPUESTA FINAL
        //
        // {
        //     "jugadores": [
        //         {
        //             "jugadorId": 1,
        //             "jugador": "Marco Bustos",
        //             "equipoId": 10,
        //             "equipo": "Prueba10",
        //             "sanciones": [],
        //             "suspensiones": []
        //         }
        //     ]
        // }
        // =====================================================

        return JugadoresSancionesSuspensionesResponse
                .builder()

                .jugadores(
                        List.of(
                                jugadorResponse
                        )
                )

                .build();
    }


    // =========================================================
    // CONSTRUIR NOMBRE DEL JUGADOR
    // =========================================================

    private String construirNombreJugador(
            Jugador jugador
    ) {

        if (jugador == null) {

            return "Jugador sin nombre";
        }


        String nombre =
                jugador.getNombre();

        String apellido =
                jugador.getApellido();


        // =====================================================
        // NOMBRE Y APELLIDO VACÍOS
        // =====================================================

        if (
                (nombre == null || nombre.isBlank()) &&
                (apellido == null || apellido.isBlank())
        ) {

            return "Jugador sin nombre";
        }


        // =====================================================
        // SOLO APELLIDO
        // =====================================================

        if (
                nombre == null ||
                nombre.isBlank()
        ) {

            return apellido.trim();
        }


        // =====================================================
        // SOLO NOMBRE
        // =====================================================

        if (
                apellido == null ||
                apellido.isBlank()
        ) {

            return nombre.trim();
        }


        // =====================================================
        // NOMBRE + APELLIDO
        // =====================================================

        return nombre.trim()
                + " "
                + apellido.trim();
    }


    // =========================================================
    // CONVERTIR SANCION -> SancionModel
    // =========================================================

    private SancionModel convertirSancionModel(
            Sancion sancion
    ) {

        SancionModel model =
                new SancionModel();


        if (sancion == null) {

            return model;
        }


        // =====================================================
        // ID
        // =====================================================

        model.setId(
                sancion.getId()
        );


        // =====================================================
        // PARTIDO
        // =====================================================

        if (
                sancion.getPartido() != null
        ) {

            model.setPartido(
                    sancion
                            .getPartido()
                            .getId()
            );
        }


        // =====================================================
        // JUGADOR
        // =====================================================

        if (
                sancion.getJugador() != null
        ) {

            model.setJugador(
                    sancion
                            .getJugador()
                            .getId()
            );
        }


        // =====================================================
        // EQUIPO TORNEO
        // =====================================================

        if (
                sancion.getEquipoTorneo() != null
        ) {

            model.setEquipoTorneo(
                    sancion
                            .getEquipoTorneo()
                            .getId()
            );
        }


        // =====================================================
        // MINUTO
        // =====================================================

        model.setMinuto(
                sancion.getMinuto()
        );


        // =====================================================
        // TIPO
        // =====================================================

        model.setTipo(
                sancion.getTipo()
        );


        // =====================================================
        // OBSERVACIÓN
        // =====================================================

        model.setObservacion(
                sancion.getObservacion()
        );


        // =====================================================
        // RETORNAR
        // =====================================================

        return model;
    }


    // =========================================================
    // CONVERTIR SUSPENSION -> SuspensionModel
    // =========================================================

    private SuspensionModel convertirSuspensionModel(
            Suspension suspension
    ) {

        SuspensionModel model =
                new SuspensionModel();


        if (suspension == null) {

            return model;
        }


        // =====================================================
        // ID
        // =====================================================

        model.setId(
                suspension.getId()
        );


        // =====================================================
        // JUGADOR
        // =====================================================

        if (
                suspension.getJugador() != null
        ) {

            model.setJugador(
                    suspension
                            .getJugador()
                            .getId()
            );
        }


        // =====================================================
        // FECHA INICIO
        //
        // La entidad utiliza LocalDate.
        // El Model también debe utilizar LocalDate.
        // =====================================================

        model.setFechaInicio(
                suspension.getFechaInicio()
        );


        // =====================================================
        // FECHA FIN
        // =====================================================

        model.setFechaFin(
                suspension.getFechaFin()
        );


        // =====================================================
        // MOTIVO
        // =====================================================

        model.setMotivo(
                suspension.getMotivo()
        );


        // =====================================================
        // RETORNAR
        // =====================================================

        return model;
    }

}
