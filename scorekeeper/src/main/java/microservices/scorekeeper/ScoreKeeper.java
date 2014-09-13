package microservices.scorekeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import microservices.scorekeeper.messages.GameStarted;
import microservices.scorekeeper.messages.TotalScore;
import microservices.scorekeeper.messages.WordScored;

import java.util.HashMap;
import java.util.Map;

public class ScoreKeeper {
    public static final String EXCHANGE_NAME = "combo";

    private ConnectionFactory connectionFactory = setUpConnectionFactory();
    private Publisher publisher = new Publisher(connectionFactory, EXCHANGE_NAME);
    private Subscriber subscriber = new Subscriber(connectionFactory, EXCHANGE_NAME, "#");
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Long> scores = new HashMap<>();

    public static ConnectionFactory setUpConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.117.95");
        factory.setPort(5672);
        return factory;
    }

    private void broadcastNewScore(String id, long score) throws Exception {
        String message = objectMapper.writeValueAsString(new TotalScore(id, score));
        publisher.publish("totalScore", message);
        System.out.println("Published '" + message + "' on totalScore");
    }

    public void doOneThing() throws Exception {
        QueueingConsumer.Delivery delivery = subscriber.getNextMessage();
        String message = new String(delivery.getBody());
        String topic = delivery.getEnvelope().getRoutingKey();

        System.out.println("Received a message on '" + topic + "' '" + message + "'");

        if (topic.equals("wordScored")) {
            WordScored wordScored = objectMapper.readValue(message, WordScored.class);
            if (!scores.containsKey(wordScored.id)) {
                System.out.println("Unknown game " + wordScored.id);
                return;
            }
            long score = scores.get(wordScored.id);
            score += wordScored.score;
            scores.put(wordScored.id, score);
            broadcastNewScore(wordScored.id, score);
        } else if (topic.equals("game.started")) {
            GameStarted gameStarted = objectMapper.readValue(message, GameStarted.class);
            scores.put(gameStarted.id, 0L);
            broadcastNewScore(gameStarted.id, 0);
        }
    }

    public void enterInfiniteLoop() throws Exception {
        while (true) {
            try {
                doOneThing();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void main(String[] argv) throws Exception {
        new ScoreKeeper().enterInfiniteLoop();
    }
}
