package com.example;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;

public class ScoreSST {

    private String wordsFoundTopic = "words.found";

    private Publisher publisher;

    @Before
    public void setUp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.117.95");
        Connection connection = factory.newConnection();
        publisher = new Publisher("combo", connection);
    }

    @Test
    public void testPublish() throws Exception {

        String w1 = "{'word': 'Hackaton', 'index': 1, 'start': 2, 'direction': 'V'}";
        String w2 = "{'word': 'Cat', 'index': 3, 'start': 4, 'direction': 'H'}";
        String message = String.format("{'id': 123, 'words':[ %s, %s ]}", w1, w2);

        publisher.publish(message.replaceAll("'", "\""), wordsFoundTopic);

    }
}
