package microservices.scorekeeper;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class Subscriber {

    private QueueingConsumer consumer;

    public Subscriber(ConnectionFactory connectionFactory, String exchangeName, String routingKey) {
        Connection connection;
        try {
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, "topic");
            String queueName = channel.queueDeclare().getQueue();
            System.out.println(queueName);
            channel.queueBind(queueName, exchangeName, routingKey);

            System.out.println("Listening for messages");

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public QueueingConsumer.Delivery getNextMessage() {
        try {
            return consumer.nextDelivery();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
