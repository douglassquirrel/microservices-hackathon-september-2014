package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Subscriber {

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Autowired
    private Connection connection;

    public void subscribe(String topicName, MessageProcessor processor) {
        try {
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, "topic");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, topicName);

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
