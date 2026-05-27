package com.senai.npsv_gestor_tintas_backend.infrastructure.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Subscriber MQTT para o tópico v1/dispositivos/+/status.
 * NPSV-308 — Monitora heartbeat (online) e LWT (offline) do ESP32.
 *
 * Nesta versão, o status é registrado apenas em log.
 * Evolução futura: persistir em tabela dispositivo_status ou cache Redis.
 */
@Slf4j
@Component
public class StatusMqttSubscriber {

    @ServiceActivator(inputChannel = "mqttStatusInputChannel")
    public void handleStatus(Message<String> message) {
        String payload = message.getPayload();
        String topico = (String) message.getHeaders().get("mqtt_receivedTopic");

        // Extrai o idDispositivo do tópico: v1/dispositivos/{id}/status
        String idDispositivo = extrairIdDispositivo(topico);

        if (payload.contains("\"offline\"")) {
            log.warn("[MQTT][STATUS] Dispositivo OFFLINE detectado | id={} | payload={}",
                    idDispositivo, payload);
        } else {
            log.info("[MQTT][STATUS] Heartbeat recebido | id={} | payload={}",
                    idDispositivo, payload);
        }
    }

    private String extrairIdDispositivo(String topico) {
        if (topico == null) return "desconhecido";
        // Formato: v1/dispositivos/{id}/status → partes[2]
        String[] partes = topico.split("/");
        return partes.length >= 3 ? partes[2] : "desconhecido";
    }
}
