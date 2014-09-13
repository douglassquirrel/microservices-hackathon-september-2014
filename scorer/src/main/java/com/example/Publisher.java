package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Publisher {

    private Channel channel;

    public Publisher() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.117.95");
        Connection connection;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void publish(String message, String topicName) {
        if (message == null) {
            return;
        }
        try {
            channel.basicPublish("combo", topicName, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" [x] Sent '" + message + "'");
    }
}
