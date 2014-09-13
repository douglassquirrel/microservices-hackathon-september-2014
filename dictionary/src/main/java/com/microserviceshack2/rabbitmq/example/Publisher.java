package com.microserviceshack2.rabbitmq.example;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceshack2.dictionary.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {

	public static void main(String[] argv) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Config.RABBIT_MQ_SERVER);
			factory.setPort(Config.RABBIT_MQ_PORT);

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Config.EXCHANGE, "topic");

			String routingKey = "topic";
			Map<String, Object> message = new HashMap<String, Object>();
			message.put("User", "Ivan");
			message.put("Data", "Hello Hackers");

			ObjectMapper mapper = new ObjectMapper();
			String string = mapper.writeValueAsString(message);

			channel.basicPublish(Config.EXCHANGE, routingKey, null,
					string.getBytes());
			System.out.println(" [x] Sent '" + routingKey + "':'" + message
					+ "'");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}
}