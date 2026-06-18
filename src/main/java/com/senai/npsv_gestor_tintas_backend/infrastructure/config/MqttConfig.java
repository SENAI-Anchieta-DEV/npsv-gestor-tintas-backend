package com.senai.npsv_gestor_tintas_backend.infrastructure.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * Configura a infraestrutura MQTT do Spring Integration.
 * NPSV-306 — Factory, canais de entrada (pesagem + status) e saída (comando).
 */
@Configuration
public class MqttConfig {


    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    @Value("${mqtt.clean-session:true}")
    private boolean cleanSession;

    @Value("${mqtt.automatic-reconnect:true}")
    private boolean automaticReconnect;

    @Value("${mqtt.keep-alive-interval:60}")
    private int keepAliveInterval;

    @Value("${mqtt.connection-timeout:30}")
    private int connectionTimeout;

    @Value("${mqtt.default-qos:1}")
    private int defaultQos;

    // --------------------------------------------------------------------------
    // FACTORY — conexão compartilhada com o broker Mosquitto
    // --------------------------------------------------------------------------

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setCleanSession(cleanSession);
        options.setAutomaticReconnect(automaticReconnect);
        options.setKeepAliveInterval(keepAliveInterval);
        options.setConnectionTimeout(connectionTimeout);

        // Credenciais opcionais — broker local geralmente usa allow_anonymous true
        if (username != null && !username.isBlank()) {
            options.setUserName(username);
            options.setPassword(password.toCharArray());
        }

        factory.setConnectionOptions(options);
        return factory;
    }

    // --------------------------------------------------------------------------
    // CANAL DE ENTRADA — tópico de pesagem (ESP32 → Backend)
    // --------------------------------------------------------------------------

    @Bean
    public MessageChannel mqttPesagemInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttPesagemInbound(MqttPahoClientFactory factory) {
        // Wildcard + → aceita qualquer idDispositivo no tópico
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                clientId + "-sub-pesagem",
                factory,
                "v1/dispositivos/+/pesagem"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(defaultQos);
        adapter.setOutputChannel(mqttPesagemInputChannel());
        return adapter;
    }

    // --------------------------------------------------------------------------
    // CANAL DE ENTRADA — tópico de status / LWT (ESP32 → Backend)
    // --------------------------------------------------------------------------

    @Bean
    public MessageChannel mqttStatusInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttStatusInbound(MqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                clientId + "-sub-status",
                factory,
                "v1/dispositivos/+/status"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(defaultQos);
        adapter.setOutputChannel(mqttStatusInputChannel());
        return adapter;
    }

    // --------------------------------------------------------------------------
    // CANAL DE SAÍDA — tópico de comando (Backend → ESP32)
    // --------------------------------------------------------------------------

    @Bean
    public MessageChannel mqttComandoOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttComandoOutputChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(
                clientId + "-pub-comando",
                factory
        );
        handler.setAsync(true);
        handler.setDefaultQos(defaultQos);
        return handler;
    }
}
