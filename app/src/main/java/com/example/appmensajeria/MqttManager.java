package com.example.appmensajeria;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MqttManager {

    private static Mqtt3AsyncClient client;

    public interface MessageCallback {
        void onNewMessage(String topic, String msg);
    }

    public static void init(MessageCallback callback) {

        client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .serverPort(1883)
                .build()
                .toAsync();

        client.connect().whenComplete((ack, throwable) -> {});
    }

    public static void subscribe(String chatId, MessageCallback callback) {
        client.subscribeWith()
                .topicFilter("chat/" + chatId)
                .callback(publish -> {
                    String msg = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                    callback.onNewMessage(publish.getTopic().toString(), msg);
                })
                .send();
    }

    public static void publish(String chatId, String msg) {
        client.publishWith()
                .topic("chat/" + chatId)
                .payload(msg.getBytes(StandardCharsets.UTF_8))
                .send();
    }
}
