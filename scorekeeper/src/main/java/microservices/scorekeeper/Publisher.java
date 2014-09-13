package microservices.scorekeeper;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Publisher {

    private final Channel channel;
    private final String exchangeName;

    public Publisher(ConnectionFactory connectionFactory, String exchangeName) {
        try {
            Connection connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.exchangeName = exchangeName;
    }

    public void publish(String topicName, String message) {
        try {
            channel.basicPublish(exchangeName, topicName, null, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
