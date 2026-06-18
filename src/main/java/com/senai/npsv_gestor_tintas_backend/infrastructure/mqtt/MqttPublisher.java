package com.senai.npsv_gestor_tintas_backend.infrastructure.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Gateway para publicar mensagens no tópico de comando do ESP32.
 * NPSV-314 — Backend → ESP32 via tópico v1/dispositivos/{id}/comando.
 *
 * Uso:
 *   mqttPublisher.publicarComando("v1/dispositivos/esp32_balanca_01/comando", payload);
 */
@MessagingGateway(defaultRequestChannel = "mqttComandoOutputChannel")
public interface MqttPublisher {
    void publicarComando(
            @Header(MqttHeaders.TOPIC) String topico,
            String payload
    );
}
