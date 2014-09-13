package letters;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public final class Consumer {

    private final QueueingConsumer consumer;
    private final String topic;

    public Consumer(final ConnectionFactory factory,
                    final String topic) throws IOException {
        this.topic = topic;
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        final String queueName = channel.queueDeclare().getQueue();
        channel.exchangeDeclare("combo", "topic");
        channel.queueBind(queueName, "combo", topic);
        this.consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
    }

    public String consumeNext() {
        try {
            final String message = new String(consumer.nextDelivery().getBody());
            System.out.printf("Received from %s: '%s'%n", topic, message);
            return message;
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
