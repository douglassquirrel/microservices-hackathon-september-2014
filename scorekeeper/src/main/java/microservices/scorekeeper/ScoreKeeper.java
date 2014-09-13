package microservices.scorekeeper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import microservices.scorekeeper.messages.GameScore;
import microservices.scorekeeper.messages.GameStarted;
import microservices.scorekeeper.messages.WordScored;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScoreKeeper {
    public static final String EXCHANGE_NAME = "combo";
    public static final String CONNECTION_HOST = "54.76.117.95";
    public static final int CONNECTION_PORT = 5672;
    public static final String GAME_SCORE_TOPIC = "game.score";
    public static final String WORD_SCORE_TOPIC = "words.scored";
    public static final String GAME_STARTED_TOPIC = "game.started";

    private ConnectionFactory connectionFactory = setUpConnectionFactory();
    private Publisher publisher = new Publisher(connectionFactory, EXCHANGE_NAME);
    private Subscriber subscriber = new Subscriber(connectionFactory, EXCHANGE_NAME, WORD_SCORE_TOPIC, GAME_STARTED_TOPIC);
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Long> scores = new HashMap<>();

    private ConnectionFactory setUpConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CONNECTION_HOST);
        factory.setPort(CONNECTION_PORT);
        return factory;
    }

    private void broadcastNewScore(String id, long score) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(new GameScore(id, score));
        publisher.publish(GAME_SCORE_TOPIC, message);
        System.out.println("Published '" + message + "' on totalScore");
    }

    private void handleWordScored(WordScored wordScored) throws JsonProcessingException {
        if (!scores.containsKey(wordScored.id)) {
            System.out.println("Unknown game " + wordScored.id);
            return;
        }
        long score = scores.get(wordScored.id);
        score += wordScored.score;
        scores.put(wordScored.id, score);
        broadcastNewScore(wordScored.id, score);
    }

    private void handleGameStarted(GameStarted gameStarted) throws JsonProcessingException {
        scores.put(gameStarted.id, 0L);
        broadcastNewScore(gameStarted.id, 0);
    }

    private void processOneMessage() throws IOException {
        QueueingConsumer.Delivery delivery = subscriber.getNextMessage();
        String message = new String(delivery.getBody());
        String topic = delivery.getEnvelope().getRoutingKey();

        System.out.println("Received a message on '" + topic + "' '" + message + "'");

        if (topic.equals(WORD_SCORE_TOPIC)) {
            WordScored wordScored = objectMapper.readValue(message, WordScored.class);
            handleWordScored(wordScored);
        } else if (topic.equals(GAME_STARTED_TOPIC)) {
            GameStarted gameStarted = objectMapper.readValue(message, GameStarted.class);
            handleGameStarted(gameStarted);
        }
    }

    public void enterInfiniteLoop() throws Exception {
        while (true) {
            try {
                processOneMessage();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ScoreKeeper().enterInfiniteLoop();
    }
}
