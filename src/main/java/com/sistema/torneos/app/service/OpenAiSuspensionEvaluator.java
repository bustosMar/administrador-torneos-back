package com.sistema.torneos.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.entity.Partido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Iterator;

@Service
public class OpenAiSuspensionEvaluator {

    private static final URI RESPONSES_API = URI.create("https://api.openai.com/v1/responses");
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${ai.openai.api-key:}")
    private String apiKey;

    @Value("${ai.openai.model:}")
    private String model;

    @Value("${ai.openai.rules-path:config/reglas-suspensiones.md}")
    private String rulesPath;

    public OpenAiSuspensionEvaluator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public EvaluacionFechasSuspension evaluar(Partido partido, Jugador jugador, int amarillasPrevias,String contexto) {
        validarConfiguracion();

        try {
            String reglas = Files.readString(Path.of(rulesPath), StandardCharsets.UTF_8);
            contexto.formatted(
                    partido.getId(),
                    partido.getFecha(),
                    jugador.getId(),
                    jugador.getNombre() + " " + jugador.getApellido(),
                    amarillasPrevias
            );

            JsonNode respuesta = solicitarEvaluacion(reglas, contexto);
            return convertirRespuesta(respuesta, partido.getFecha());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No fue posible evaluar la suspensión con IA.", e);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible evaluar la suspensión con IA.", e);
        }
    }

    private JsonNode solicitarEvaluacion(String reglas, String contexto) throws IOException, InterruptedException {
        JsonNode cuerpo = objectMapper.createObjectNode()
                .put("model", model)
                .put("instructions", """
                        Usa las reglas proporcionadas. Responde solo JSON válido, sin Markdown, con esta forma:
                        {"fechaInicio":"YYYY-MM-DD","fechaFin":"YYYY-MM-DD","motivo":"texto breve"}.
                        """)
                .put("input", "REGLAS:\n" + reglas + "\n\nCASO:\n" + contexto);

        HttpRequest request = HttpRequest.newBuilder(RESPONSES_API)
                .timeout(Duration.ofSeconds(45))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(cuerpo)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("La IA no pudo evaluar la suspensión (HTTP " + response.statusCode() + ").");
        }

        return objectMapper.readTree(response.body());
    }

    private EvaluacionFechasSuspension convertirRespuesta(JsonNode respuesta, LocalDate fechaPartido) throws IOException {
        String texto = extraerTexto(respuesta.path("output"));
        if (texto == null || texto.isBlank()) {
            throw new IllegalStateException("La IA no devolvió una evaluación de suspensión válida.");
        }

        JsonNode evaluacion = objectMapper.readTree(texto.replace("```json", "").replace("```", "").trim());
        LocalDate fechaInicio = LocalDate.parse(evaluacion.path("fechaInicio").asText());
        LocalDate fechaFin = LocalDate.parse(evaluacion.path("fechaFin").asText());
        String motivo = evaluacion.path("motivo").asText().trim();

        if (fechaInicio.isBefore(fechaPartido) || fechaFin.isBefore(fechaInicio) || motivo.isBlank()) {
            throw new IllegalStateException("La IA devolvió fechas o motivo de suspensión inválidos.");
        }

        return new EvaluacionFechasSuspension(fechaInicio, fechaFin, motivo);
    }

    private String extraerTexto(JsonNode nodo) {
        if (nodo.isObject() && nodo.has("text") && nodo.get("text").isTextual()) {
            return nodo.get("text").asText();
        }

        if (nodo.isContainerNode()) {
            Iterator<JsonNode> elementos = nodo.elements();
            while (elementos.hasNext()) {
                String texto = extraerTexto(elementos.next());
                if (texto != null) {
                    return texto;
                }
            }
        }

        return null;
    }

    private void validarConfiguracion() {
        if (apiKey == null || apiKey.isBlank() || model == null || model.isBlank()) {
            throw new IllegalStateException("Configure OPENAI_API_KEY y OPENAI_MODEL para evaluar suspensiones con IA.");
        }

        if (!Files.isRegularFile(Path.of(rulesPath))) {
            throw new IllegalStateException("No se encontró el archivo de reglas de suspensión: " + rulesPath);
        }
    }
}
