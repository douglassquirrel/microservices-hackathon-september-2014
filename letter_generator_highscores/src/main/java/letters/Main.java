package letters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Main {

    public static void main(final String[] args) throws IOException, InterruptedException {
        final LetterGenerator generator = new LetterGenerator(Main.class.getResourceAsStream("/horse.json"));

        final ConnectionFactory connectionFactory = connectionFactory();

        final WordsConsumer wordsConsumer = new WordsConsumer(
                new Consumer(connectionFactory, "words_found"),
                new ObjectMapper());

        final LetterPublisher publisher = new LetterPublisher(
                new Publisher(connectionFactory, "letter.created"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    Consumer consumer = new Consumer(connectionFactory(), "game.started");

                    while (true) {
                        String body = consumer.consumeNext();
                        GameStarted gameStarted = new ObjectMapper().readValue(body, GameStarted.class);

                        publisher.publishLetterForGame(gameStarted.id, generator.nextLetter());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        while (true) {
            final Map<String, Object> message = wordsConsumer.receiveNextWords();
            final String gameId = (String) message.get("id");
            final Collection<Object> words = (Collection<Object>) message.get("words");
            if (words != null) {
                if (words.isEmpty()) {
                    publisher.publishLetterForGame(gameId, generator.nextLetter());
                }
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameStarted {
        public String id;
    }

    private static ConnectionFactory connectionFactory() {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.117.95");
        return factory;
    }
}
