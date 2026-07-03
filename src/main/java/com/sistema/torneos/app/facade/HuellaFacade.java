package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.web.model.response.HuellaResponse;
import com.sistema.torneos.app.web.model.response.LectorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class HuellaFacade {

    @Value("${biometria.capture.jar}")
    private String biometriaJar;

    @Value("${biometria.sdk.path}")
    private String sdkPath;

    private volatile boolean escuchando = false;
    private volatile String ultimoTemplateBase64;
    private volatile String ultimoDedo;
    private volatile String ultimoMensaje = "No se ha capturado ninguna huella.";

    public synchronized LectorResponse escucharLector() {

        if (escuchando) {
            return LectorResponse.builder()
                    .success(true)
                    .mensaje("Ya hay una captura en proceso.")
                    .escuchando(true)
                    .build();
        }

        escuchando = true;
        ultimoTemplateBase64 = null;
        ultimoDedo = null;
        ultimoMensaje = "Abriendo capturador biométrico...";

        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "java",
                        "-Djava.awt.headless=false",
                        "-Djava.library.path=" + sdkPath,
                        "-jar",
                        biometriaJar
                );

                processBuilder.redirectErrorStream(true);

                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        System.out.println("[BIOMETRIA] " + line);

                        if (line.startsWith("OK|")) {
                            String[] partes = line.split("\\|", 3);

                            if (partes.length == 3) {
                                ultimoDedo = partes[1];
                                ultimoTemplateBase64 = partes[2];
                                ultimoMensaje = "Huella capturada correctamente.";
                            }
                        }

                        if (line.startsWith("ERROR|")) {
                            ultimoMensaje = line.substring("ERROR|".length());
                        }
                    }
                }

                int exitCode = process.waitFor();

                if (exitCode != 0 && ultimoTemplateBase64 == null) {
                    ultimoMensaje = "El capturador finalizó con error. Código: " + exitCode;
                }

            } catch (Exception e) {
                ultimoMensaje = "Error ejecutando capturador biométrico: " + e.getMessage();
                e.printStackTrace();

            } finally {
                escuchando = false;
            }
        }).start();

        return LectorResponse.builder()
                .success(true)
                .mensaje("Capturador biométrico abierto. Coloca el dedo en el lector.")
                .escuchando(true)
                .build();
    }

    public HuellaResponse obtenerHuella() {

        if (ultimoTemplateBase64 == null || ultimoTemplateBase64.isBlank()) {
            return HuellaResponse.builder()
                    .success(false)
                    .mensaje(ultimoMensaje)
                    .dedo(null)
                    .templateBase64(null)
                    .build();
        }

        return HuellaResponse.builder()
                .success(true)
                .mensaje("Huella obtenida correctamente.")
                .dedo(ultimoDedo)
                .templateBase64(ultimoTemplateBase64)
                .build();
    }

    public synchronized LectorResponse detenerLector() {

        escuchando = false;
        ultimoMensaje = "Captura detenida.";

        return LectorResponse.builder()
                .success(true)
                .mensaje(ultimoMensaje)
                .escuchando(false)
                .build();
    }
}