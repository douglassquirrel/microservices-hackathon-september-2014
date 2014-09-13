package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ApplicationConfiguration {

    @Value("${rabbitmq.host}")
    private String rabbitmqHost;

    @Bean
    Connection connection() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        return factory.newConnection();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
