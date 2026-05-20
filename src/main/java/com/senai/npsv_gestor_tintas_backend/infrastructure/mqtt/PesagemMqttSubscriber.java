package com.senai.npsv_gestor_tintas_backend.infrastructure.mqtt;

import com.senai.npsv_gestor_tintas_backend.application.service.PesagemMqttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Subscriber MQTT para o tópico v1/dispositivos/+/pesagem.
 * NPSV-307 — Deserializa o payload do ESP32 e delega ao PesagemMqttService.
 *
 * O try-catch aqui garante que o subscriber nunca morre por causa de um
 * payload malformado ou producaoId inválido (NPSV-313).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PesagemMqttSubscriber {

    private final PesagemMqttService pesagemMqttService;

    @ServiceActivator(inputChannel = "mqttPesagemInputChannel")
    public void handlePesagem(Message<String> message) {
        String payload = message.getPayload();
        String topico = (String) message.getHeaders().get("mqtt_receivedTopic");

        log.debug("[MQTT] Mensagem recebida | tópico: {} | payload: {}", topico, payload);

        try {
            pesagemMqttService.processarPayloadMqtt(payload);
        } catch (Exception e) {
            // Captura qualquer exceção não prevista para garantir que o subscriber
            // continue ativo e processe a próxima mensagem normalmente.
            log.error("[MQTT] Erro inesperado ao processar pesagem | tópico: {} | erro: {}",
                    topico, e.getMessage(), e);
        }
    }
}
