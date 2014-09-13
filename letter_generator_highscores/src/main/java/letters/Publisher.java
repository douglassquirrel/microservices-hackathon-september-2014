package letters;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

final class Publisher {

    private final String topic;
    private final Channel channel;

    public Publisher(final ConnectionFactory factory,
                     final String topic) throws IOException {
        this.topic = topic;
        final Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        final String queueName = channel.queueDeclare().getQueue();
        channel.exchangeDeclare("combo", "topic");
        channel.queueBind(queueName, "combo", topic);
    }

    public void publish(final String messageBody) {
        try {
            System.out.printf("Publishing to %s: '%s'%n", topic, messageBody);
            channel.basicPublish("combo", topic, null, messageBody.getBytes());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
