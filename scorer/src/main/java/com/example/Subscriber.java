package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class Subscriber {

    private static final String EXCHANGE_NAME = "combo";

    public void subscribe(String topicName, MessageProcessor processor) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("54.76.117.95");
            Connection connection = null;
            connection = factory.newConnection();

            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, topicName);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received '" + message + "' Key: " + delivery.getEnvelope().getRoutingKey());

                processor.process(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static interface MessageProcessor {

        void process(String message);

    }

}
