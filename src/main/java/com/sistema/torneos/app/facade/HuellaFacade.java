package com.sistema.torneos.app.facade;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPTemplateStatus;
import com.sistema.torneos.app.web.model.response.HuellaResponse;
import com.sistema.torneos.app.web.model.response.LectorResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class HuellaFacade {

    private DPFPCapture capturador;
    private DPFPEnrollment enrollment;

    private volatile boolean escuchando = false;
    private volatile boolean lectorConectado = false;

    private volatile String ultimoTemplateBase64;
    private volatile String ultimoMensaje = "No se ha capturado ninguna huella.";

    public synchronized LectorResponse escucharLector() {

        if (escuchando) {
            return LectorResponse.builder()
                    .success(true)
                    .mensaje("El lector ya se encuentra escuchando.")
                    .escuchando(true)
                    .build();
        }

        try {
            ultimoTemplateBase64 = null;
            ultimoMensaje = "Esperando huella...";

            enrollment = DPFPGlobal.getEnrollmentFactory().createEnrollment();
            capturador = DPFPGlobal.getCaptureFactory().createCapture();

            configurarEventos();

            capturador.startCapture();
            escuchando = true;

            return LectorResponse.builder()
                    .success(true)
                    .mensaje("Escucha iniciada. Coloca el dedo en el lector.")
                    .escuchando(true)
                    .build();

        } catch (Exception e) {
            escuchando = false;
            ultimoMensaje = "Error al iniciar lector: " + e.getMessage();

            return LectorResponse.builder()
                    .success(false)
                    .mensaje(ultimoMensaje)
                    .escuchando(false)
                    .build();
        }
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
                .dedo("UNKNOWN")
                .templateBase64(ultimoTemplateBase64)
                .build();
    }

    private void configurarEventos() {

        capturador.addDataListener(new DPFPDataAdapter() {

            @Override
            public void dataAcquired(DPFPDataEvent event) {
            	
            	System.out.println("Huella detectada por el lector");
            	
                try {
                    DPFPSample sample = event.getSample();

                    DPFPFeatureSet features = DPFPGlobal
                            .getFeatureExtractionFactory()
                            .createFeatureExtraction()
                            .createFeatureSet(
                                    sample,
                                    DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT
                            );

                    enrollment.addFeatures(features);

                    if (enrollment.getTemplateStatus()
                        == DPFPTemplateStatus.TEMPLATE_STATUS_READY) {

                        DPFPTemplate template = enrollment.getTemplate();

                        ultimoTemplateBase64 = Base64.getEncoder()
                                .encodeToString(template.serialize());

                        ultimoMensaje = "Huella capturada correctamente.";

                        detenerCaptura();
                    } else {
                        ultimoMensaje = "Muestra capturada. Coloca el dedo nuevamente.";
                    }

                } catch (Exception e) {
                    ultimoMensaje = "Error procesando huella: " + e.getMessage();

                    try {
                        enrollment.clear();
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        capturador.addReaderStatusListener(new DPFPReaderStatusAdapter() {

            @Override
            public void readerConnected(DPFPReaderStatusEvent event) {
                lectorConectado = true;
                ultimoMensaje = "Lector conectado.";
            }

            @Override
            public void readerDisconnected(DPFPReaderStatusEvent event) {
                lectorConectado = false;
                ultimoMensaje = "Lector desconectado.";
                detenerCaptura();
            }
        });

      capturador.addErrorListener(new DPFPErrorAdapter() {

        @Override
        public void errorOccured(DPFPErrorEvent event) {
            ultimoMensaje = "Error del lector: " + event.getError();
            System.out.println(ultimoMensaje);
            detenerCaptura();
        }

        @Override
        public void exceptionCaught(DPFPErrorEvent event) {
            ultimoMensaje = "Excepción del lector: " + event.getError();
            System.out.println(ultimoMensaje);
            detenerCaptura();
        }
    });
}

    private synchronized void detenerCaptura() {
        try {
            if (capturador != null) {
                capturador.stopCapture();
            }
        } catch (Exception ignored) {
        } finally {
            escuchando = false;
        }
    }
}