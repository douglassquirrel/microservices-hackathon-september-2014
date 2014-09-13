package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class Application {

    public static final String WORDS_FOUND_TOPIC = "wordsFound";
    public static final String WORD_SCORED_TOPIC = "wordScored";

    public static void main(String[] args) throws IOException, InterruptedException {

        final Subscriber subscriber = new Subscriber();
        final Publisher publisher = new Publisher();
        final ScoreProcessor scoreProcessor = new ScoreProcessor();

        subscriber.subscribe(WORDS_FOUND_TOPIC, new Subscriber.MessageProcessor() {

            @Override
            public void process(String message) {
                String response = scoreProcessor.process(message);
                publisher.publish(response, WORD_SCORED_TOPIC);
            }

        });

    }

}
