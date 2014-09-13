package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

    @Value("${topic.wordsFound}")
    private String wordsFoundTopic;

    @Value("${topic.wordScored}")
    private String wordsScoredTopic;

    @Autowired
    private ScoreProcessor scoreProcessor;

    @Autowired
    private Subscriber subscriber;

    @Autowired
    private Publisher publisher;

    @Override
    public void run(String... strings) throws Exception {
        subscriber.subscribe(wordsFoundTopic, new Subscriber.MessageProcessor() {

            @Override
            public void process(String message) {
                String response = scoreProcessor.process(message);
                publisher.publish(response, wordsScoredTopic);
            }

        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Application.class, args);
    }

}
