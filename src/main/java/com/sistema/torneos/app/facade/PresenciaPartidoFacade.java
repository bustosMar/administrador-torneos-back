package com.sistema.torneos.app.facade;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.repository.JugadorEnEquipoRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.domain.repository.PresenciaPartidoRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.request.GuardarPresenciasPartidoRequest;
import com.sistema.torneos.app.web.model.request.RegistrarPresenciaHuellaRequest;
import com.sistema.torneos.app.web.model.response.JugadorPartidoResponse;
import com.sistema.torneos.app.web.model.response.GuardarPresenciasPartidoResponse;
import com.sistema.torneos.app.web.model.response.PresenciaPartidoDetalleResponse;
import com.sistema.torneos.app.web.model.response.RegistroPresenciaHuellaResponse;

import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PresenciaPartidoFacade {

    private final PresenciaPartidoRepository presenciaPartidoRepository;
    private final PartidoRepository partidoRepository;
    private final JugadorEnEquipoRepository jugadorEnEquipoRepository;

    @Autowired
    public PresenciaPartidoFacade(
            PresenciaPartidoRepository presenciaPartidoRepository,
            PartidoRepository partidoRepository,
            JugadorEnEquipoRepository jugadorEnEquipoRepository) {
        this.presenciaPartidoRepository = presenciaPartidoRepository;
        this.partidoRepository = partidoRepository;
        this.jugadorEnEquipoRepository = jugadorEnEquipoRepository;
    }

    public List<PresenciaPartido> findAll() {
        return presenciaPartidoRepository.findAll();
    }

    public PresenciaPartido findById(Long id) {
        return presenciaPartidoRepository.findById(id).orElse(null);
    }

    public PresenciaPartidoDetalleResponse getDetalleByPartido(Long idPartido) {

    Partido partido = partidoRepository.findById(idPartido)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el partido con id: " + idPartido));

    List<JugadorEnEquipo> jugadoresElegibles = obtenerJugadoresElegibles(partido);
    List<PresenciaPartido> presencias = presenciaPartidoRepository.findByPartido_IdOrderByJugador_NombreAscJugador_ApellidoAsc(idPartido);
    Set<Long> jugadoresPresentes = presencias.stream()
            .map(presencia -> presencia.getJugador().getId())
            .collect(Collectors.toSet());

    List<JugadorPartidoResponse> jugadoresLocal = new ArrayList<>();
    List<JugadorPartidoResponse> jugadoresVisitante = new ArrayList<>();

    for (JugadorEnEquipo jugadorEnEquipo : jugadoresElegibles) {
        JugadorPartidoResponse dto = mapJugador(jugadorEnEquipo, jugadoresPresentes.contains(jugadorEnEquipo.getJugador().getId()));

        if (jugadorEnEquipo.getEquipo().getId().equals(partido.getEquipoLocal().getEquipo().getId())) {
            jugadoresLocal.add(dto);
        } else {
            jugadoresVisitante.add(dto);
        }
    }

    PresenciaPartidoDetalleResponse response = new PresenciaPartidoDetalleResponse();
    response.setIdPartido(partido.getId());
    response.setNumeroJornada(partido.getJornada().getNumeroJornada());
    response.setTorneo(partido.getJornada().getTorneo().getNombre());
    response.setFecha(partido.getFecha() != null ? partido.getFecha().toString() : null);
    response.setHora(partido.getHora());
    response.setIdEquipoLocal(partido.getEquipoLocal().getEquipo().getId());
    response.setEquipoLocal(partido.getEquipoLocal().getEquipo().getNombre());
    response.setIdEquipoVisitante(partido.getEquipoVisitante().getEquipo().getId());
    response.setEquipoVisitante(partido.getEquipoVisitante().getEquipo().getNombre());
    response.setMunicipioEstado(obtenerMunicipioEstado(partido));
    response.setJugadoresLocal(jugadoresLocal);
    response.setJugadoresVisitante(jugadoresVisitante);
    response.setPresenciasRegistradas(mapPresencias(presencias, partido, jugadoresElegibles));

    return response;
    }

    @Transactional
    public RegistroPresenciaHuellaResponse registrarPorHuella(Long idPartido, RegistrarPresenciaHuellaRequest request) {

    if (request == null || request.getEquipoId() == null) {
	    throw new ResourceNotFoundException("Debe indicar el equipo para registrar la presencia.");
	}

    if (request.getHuella() == null || request.getHuella().isBlank()) {
        throw new ResourceNotFoundException("No se recibió una huella válida para registrar la presencia.");
    }

    Partido partido = partidoRepository.findById(idPartido)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el partido con id: " + idPartido));

    List<JugadorEnEquipo> jugadoresElegibles = obtenerJugadoresElegibles(partido);

    JugadorEnEquipo jugadorDetectado = identificarJugadorPorHuella(partido, jugadoresElegibles, request.getHuella(), request.getEquipoId());

    Optional<PresenciaPartido> presenciaExistente = presenciaPartidoRepository.findByPartido_IdAndJugador_Id(idPartido, jugadorDetectado.getJugador().getId());

    RegistroPresenciaHuellaResponse response = new RegistroPresenciaHuellaResponse();
    response.setRegistrada(false);
    response.setMensaje(presenciaExistente.isPresent()
            ? "El jugador ya tiene presencia registrada en este partido."
            : "Jugador identificado correctamente. Listo para agregarse a la lista temporal.");
    response.setJugador(mapJugador(jugadorDetectado, true));
    response.setPresenciasRegistradas(mapPresencias(
            presenciaPartidoRepository.findByPartido_IdOrderByJugador_NombreAscJugador_ApellidoAsc(idPartido),
            partido,
            jugadoresElegibles));

    return response;
    }

    @Transactional
    public GuardarPresenciasPartidoResponse guardarPresencias(Long idPartido, GuardarPresenciasPartidoRequest request) {

	if (request == null || request.getJugadores() == null || request.getJugadores().isEmpty()) {
	    throw new ResourceNotFoundException("No hay jugadores pendientes para guardar.");
	}

	Partido partido = partidoRepository.findById(idPartido)
	        .orElseThrow(() -> new ResourceNotFoundException("No existe el partido con id: " + idPartido));

	List<JugadorEnEquipo> jugadoresElegibles = obtenerJugadoresElegibles(partido);
	List<Long> jugadoresElegiblesIds = jugadoresElegibles.stream()
	        .map(jugadorEnEquipo -> jugadorEnEquipo.getJugador().getId())
	        .toList();

	for (Long idJugador : request.getJugadores()) {
	    if (!jugadoresElegiblesIds.contains(idJugador)) {
	        throw new ResourceNotFoundException("Uno de los jugadores no está ligado a los equipos de este partido.");
	    }

	    boolean yaExiste = presenciaPartidoRepository.findByPartido_IdAndJugador_Id(idPartido, idJugador).isPresent();

	    if (!yaExiste) {
	        Jugador jugador = jugadoresElegibles.stream()
	                .map(JugadorEnEquipo::getJugador)
	                .filter(item -> item.getId().equals(idJugador))
	                .findFirst()
	                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el jugador a registrar."));

	        PresenciaPartido presencia = new PresenciaPartido();
	        presencia.setPartido(partido);
	        presencia.setJugador(jugador);
	        presencia.setObservaciones(request.getObservaciones());
	        presenciaPartidoRepository.save(presencia);
	    }
	}

	List<PresenciaPartido> presenciasActualizadas = presenciaPartidoRepository.findByPartido_IdOrderByJugador_NombreAscJugador_ApellidoAsc(idPartido);

	GuardarPresenciasPartidoResponse response = new GuardarPresenciasPartidoResponse();
	response.setMensaje("Presencias guardadas correctamente.");
	response.setPresenciasRegistradas(mapPresencias(presenciasActualizadas, partido, jugadoresElegibles));

	return response;
    }

    @Transactional
    public PresenciaPartido create(PresenciaPartido presenciaPartido) {
        return presenciaPartidoRepository.save(presenciaPartido);
    }

    @Transactional
    public PresenciaPartido update(Long id, PresenciaPartido presenciaPartido) {
        if (presenciaPartidoRepository.existsById(id)) {
            presenciaPartido.setId(id);
            return presenciaPartidoRepository.save(presenciaPartido);
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (presenciaPartidoRepository.existsById(id)) {
            presenciaPartidoRepository.deleteById(id);
        }
    }

    private List<JugadorEnEquipo> obtenerJugadoresElegibles(Partido partido) {
    return jugadorEnEquipoRepository.findActivosByEquiposTorneoYCategoria(
            partido.getJornada().getTorneo().getId(),
            partido.getEquipoLocal().getCategoriaTorneo().getId(),
            List.of(
                    partido.getEquipoLocal().getEquipo().getId(),
                    partido.getEquipoVisitante().getEquipo().getId()
            )
    );
    }

    private JugadorPartidoResponse mapJugador(JugadorEnEquipo jugadorEnEquipo, boolean presente) {
    Jugador jugador = jugadorEnEquipo.getJugador();

    JugadorPartidoResponse dto = new JugadorPartidoResponse();
    dto.setIdJugador(jugador.getId());
    dto.setNombreCompleto(jugador.getNombre() + " " + jugador.getApellido());
    dto.setFoto(jugador.getFoto());
    dto.setIdEquipo(jugadorEnEquipo.getEquipo().getId());
    dto.setEquipo(jugadorEnEquipo.getEquipo().getNombre());
    dto.setPresente(presente);

    return dto;
    }

    private JugadorEnEquipo identificarJugadorPorHuella(Partido partido, List<JugadorEnEquipo> jugadoresElegibles, String huella, Long equipoId) {
    boolean equipoValido = partido.getEquipoLocal().getEquipo().getId().equals(equipoId)
            || partido.getEquipoVisitante().getEquipo().getId().equals(equipoId);

    if (!equipoValido) {
        throw new ResourceNotFoundException("El equipo seleccionado no pertenece al partido indicado.");
    }

    DPFPFeatureSet featureSet = crearFeatureSet(huella);

    Optional<JugadorEnEquipo> coincidenciaEquipo = jugadoresElegibles.stream()
            .filter(jugadorEnEquipo -> jugadorEnEquipo.getEquipo().getId().equals(equipoId))
            .filter(jugadorEnEquipo -> coincideHuella(featureSet, jugadorEnEquipo.getJugador().getHuella()))
            .findFirst();

    if (coincidenciaEquipo.isPresent()) {
        return coincidenciaEquipo.get();
    }

    boolean coincideOtroEquipo = jugadoresElegibles.stream()
            .filter(jugadorEnEquipo -> !jugadorEnEquipo.getEquipo().getId().equals(equipoId))
            .anyMatch(jugadorEnEquipo -> coincideHuella(featureSet, jugadorEnEquipo.getJugador().getHuella()));

    if (coincideOtroEquipo) {
        throw new ResourceNotFoundException("La huella corresponde a un jugador del otro equipo del partido.");
    }

    throw new ResourceNotFoundException("La huella capturada no corresponde a ningún jugador de este partido.");
    }

    private DPFPFeatureSet crearFeatureSet(String huellaVerificacion) {
    try {
        return DPFPGlobal.getFeatureSetFactory()
                .createFeatureSet(Base64.getDecoder().decode(huellaVerificacion));
    } catch (Exception e) {
        throw new ResourceNotFoundException("La huella de verificación recibida no es válida.");
    }
    }

    private boolean coincideHuella(DPFPFeatureSet featureSet, String huellaRegistrada) {
    if (huellaRegistrada == null || huellaRegistrada.isBlank()) {
        return false;
    }

    try {
        DPFPTemplate template = DPFPGlobal.getTemplateFactory()
                .createTemplate(Base64.getDecoder().decode(huellaRegistrada));

        DPFPVerification verification = DPFPGlobal.getVerificationFactory().createVerification();
        verification.setFARRequested(DPFPVerification.MEDIUM_SECURITY_FAR);

        return verification.verify(featureSet, template).isVerified();
    } catch (Exception e) {
        return false;
    }
    }

    private String obtenerMunicipioEstado(Partido partido) {
    if (partido.getJornada().getTorneo().getMunicipioEstado() == null) {
        return null;
    }

    String municipio = partido.getJornada().getTorneo().getMunicipioEstado().getMunicipio() != null
            ? partido.getJornada().getTorneo().getMunicipioEstado().getMunicipio().getNombre()
            : null;
    String estado = partido.getJornada().getTorneo().getMunicipioEstado().getEstado() != null
            ? partido.getJornada().getTorneo().getMunicipioEstado().getEstado().getNombre()
            : null;

    if (municipio == null && estado == null) {
        return null;
    }

    return municipio == null ? estado : estado == null ? municipio : municipio + ", " + estado;
    }

    private List<JugadorPartidoResponse> mapPresencias(
            List<PresenciaPartido> presencias,
            Partido partido,
            List<JugadorEnEquipo> jugadoresElegibles) {
    return presencias.stream()
            .map(presencia -> {
                Jugador jugador = presencia.getJugador();
                Long idEquipoLocal = partido.getEquipoLocal().getEquipo().getId();
                Long idEquipo = encontrarEquipoJugador(jugadoresElegibles, jugador.getId(), idEquipoLocal);

                JugadorPartidoResponse dto = new JugadorPartidoResponse();
                dto.setIdJugador(jugador.getId());
                dto.setNombreCompleto(jugador.getNombre() + " " + jugador.getApellido());
                dto.setFoto(jugador.getFoto());
                dto.setIdEquipo(idEquipo);
                dto.setEquipo(idEquipo.equals(idEquipoLocal)
                        ? partido.getEquipoLocal().getEquipo().getNombre()
                        : partido.getEquipoVisitante().getEquipo().getNombre());
                dto.setPresente(true);
                return dto;
            })
            .toList();
    }

            private Long encontrarEquipoJugador(List<JugadorEnEquipo> jugadoresElegibles, Long idJugador, Long idEquipoLocal) {
            return jugadoresElegibles.stream()
            .filter(jugadorEnEquipo -> jugadorEnEquipo.getJugador().getId().equals(idJugador))
            .map(jugadorEnEquipo -> jugadorEnEquipo.getEquipo().getId())
            .findFirst()
            .orElse(idEquipoLocal);
    }
}
