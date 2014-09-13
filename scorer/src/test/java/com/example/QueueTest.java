package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class QueueTest {
    private static final String QUEUE_NAME = "alex2";
    public static final String BASE_URL = "http://54.76.117.95:8080";

    @Test
    public void testQueue() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.183.35");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String message = "{\"sku\": \"1245\", \"name\": \"ZZZZZZZ\", \"price\": \"12.45\"}";
        channel.basicPublish(QUEUE_NAME, "room_created", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }

    @Test
    public void testRest() throws Exception {
        String result = new RestTemplate(new SimpleClientHttpRequestFactory())
                .getForObject(BASE_URL + "/topics", String.class);
        System.out.println("result = " + result);
    }

    @Test
    public void testRestRoomCreated() throws Exception {
        String result = new RestTemplate(new SimpleClientHttpRequestFactory())
                .getForObject(BASE_URL + "/topics/random/", String.class);

        System.out.println("result = " + result);
        List[] object = new ObjectMapper().readValue(result, TypeFactory.defaultInstance().constructArrayType(List.class));
        for (List list : object) {
            System.out.println("list = " + list);
        }
    }

    @Test
    public void testPublish() throws Exception {
        new Publisher().publish("{\"message\": \"Hi subscribers!\"}", "random");
    }
}
