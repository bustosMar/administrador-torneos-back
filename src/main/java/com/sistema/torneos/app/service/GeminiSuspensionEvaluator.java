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

@Service
public class GeminiSuspensionEvaluator {

    private static final String GEMINI_ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;


    @Value("${ai.gemini.api-key:}")
    private String apiKey;

    @Value("${ai.gemini.model:}")
    private String model;

    @Value("${ai.gemini.rules-path:config/reglas-suspensiones.md}")
    private String rulesPath;


    public GeminiSuspensionEvaluator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }


    public EvaluacionFechasSuspension evaluar(
            Partido partido,
            Jugador jugador,
            int amarillasPrevias,
            String contexto) {

        validarConfiguracion();

        try {

            String reglas = Files.readString(
                    Path.of(rulesPath),
                    StandardCharsets.UTF_8
            );


            String caso = """
                    Partido ID: %s
                    Fecha partido: %s
                    Jugador ID: %s
                    Jugador: %s
                    Amarillas previas: %s
                    
                    Contexto:
                    %s
                    """.formatted(
                    partido.getId(),
                    partido.getFecha(),
                    jugador.getId(),
                    jugador.getNombre() + " " + jugador.getApellido(),
                    amarillasPrevias,
                    contexto
            );


            JsonNode respuesta = solicitarEvaluacion(reglas, caso);

            return convertirRespuesta(
                    respuesta,
                    partido.getFecha()
            );


        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "No fue posible evaluar la suspensión con IA.",
                    e
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No fue posible evaluar la suspensión con IA.",
                    e
            );
        }
    }


    private JsonNode solicitarEvaluacion(
            String reglas,
            String contexto)
            throws IOException, InterruptedException {


        String prompt = """
                Usa las reglas proporcionadas.
                
                Responde únicamente JSON válido sin Markdown:
                
                {
                  "fechaInicio":"YYYY-MM-DD",
                  "fechaFin":"YYYY-MM-DD",
                  "motivo":"texto breve"
                }
                
                REGLAS:
                %s
                
                CASO:
                %s
                """.formatted(
                reglas,
                contexto
        );


        JsonNode body = objectMapper.createObjectNode()
                .set(
                        "contents",
                        objectMapper.createArrayNode()
                                .add(
                                        objectMapper.createObjectNode()
                                                .set(
                                                        "parts",
                                                        objectMapper.createArrayNode()
                                                                .add(
                                                                        objectMapper.createObjectNode()
                                                                                .put("text", prompt)
                                                                )
                                                )
                                )
                );


        URI endpoint = URI.create(
                GEMINI_ENDPOINT.formatted(model)
        );


        HttpRequest request =
                HttpRequest.newBuilder(endpoint)
                        .timeout(Duration.ofSeconds(45))
                        .header(
                                "x-goog-api-key",
                                apiKey
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        objectMapper.writeValueAsString(body)
                                )
                        )
                        .build();



        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        if(response.statusCode() < 200 ||
                response.statusCode() >= 300){

            throw new IllegalStateException(
                    "Gemini respondió HTTP "
                            + response.statusCode()
                            + ": "
                            + response.body()
            );
        }


        return objectMapper.readTree(
                response.body()
        );
    }



    private EvaluacionFechasSuspension convertirRespuesta(
            JsonNode respuesta,
            LocalDate fechaPartido)
            throws IOException {


        String texto =
                respuesta
                        .path("candidates")
                        .path(0)
                        .path("content")
                        .path("parts")
                        .path(0)
                        .path("text")
                        .asText();


        if(texto == null || texto.isBlank()){

            throw new IllegalStateException(
                    "Gemini no devolvió evaluación."
            );
        }


        JsonNode evaluacion =
                objectMapper.readTree(
                        texto
                                .replace("```json","")
                                .replace("```","")
                                .trim()
                );


        LocalDate fechaInicio =
                LocalDate.parse(
                        evaluacion.path("fechaInicio").asText()
                );


        LocalDate fechaFin =
                LocalDate.parse(
                        evaluacion.path("fechaFin").asText()
                );


        String motivo =
                evaluacion.path("motivo")
                        .asText()
                        .trim();



        if(fechaInicio.isBefore(fechaPartido)
                || fechaFin.isBefore(fechaInicio)
                || motivo.isBlank()){

            throw new IllegalStateException(
                    "Gemini devolvió fechas inválidas."
            );
        }


        return new EvaluacionFechasSuspension(
                fechaInicio,
                fechaFin,
                motivo
        );
    }



    private void validarConfiguracion(){

        if(apiKey == null || apiKey.isBlank()
                || model == null || model.isBlank()){

            throw new IllegalStateException(
                    "Configure GEMINI_API_KEY y GEMINI_MODEL para evaluar suspensiones."
            );
        }


        if(!Files.isRegularFile(Path.of(rulesPath))){

            throw new IllegalStateException(
                    "No existe el archivo de reglas: "
                            + rulesPath
            );
        }
    }
}