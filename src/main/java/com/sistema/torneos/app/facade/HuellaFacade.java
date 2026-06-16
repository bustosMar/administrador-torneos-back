package com.sistema.torneos.app.facade;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;

@Component
public class HuellaFacade {

    public String capturarTemplate() {

        try {

            CompletableFuture<String> future =
                    new CompletableFuture<>();

            DPFPCapture capturer =
                    DPFPGlobal.getCaptureFactory()
                            .createCapture();

            DPFPEnrollment enrollment =
                    DPFPGlobal.getEnrollmentFactory()
                            .createEnrollment();

            capturer.addDataListener(
                    new DPFPDataAdapter() {

                        @Override
                        public void dataAcquired(
                                DPFPDataEvent event) {

                            try {

                                DPFPSample sample =
                                        event.getSample();

                                DPFPFeatureExtraction extractor =
                                        DPFPGlobal
                                                .getFeatureExtractionFactory()
                                                .createFeatureExtraction();

                                DPFPFeatureSet features =
                                        extractor.createFeatureSet(
                                                sample,
                                                DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT
                                        );

                                enrollment.addFeatures(features);

                                if (enrollment.getFeaturesNeeded() == 0) {

                                    DPFPTemplate template =
                                            enrollment.getTemplate();

                                    String base64 =
                                            Base64.getEncoder()
                                                    .encodeToString(
                                                            template.serialize()
                                                    );

                                    capturer.stopCapture();

                                    future.complete(base64);
                                }

                            } catch (Exception ex) {

                                future.completeExceptionally(ex);
                            }
                        }
                    });

            capturer.startCapture();

            return future.get();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error al capturar huella",
                    ex
            );
        }
    }
}