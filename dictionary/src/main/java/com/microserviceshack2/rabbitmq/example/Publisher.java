package com.microserviceshack2.rabbitmq.example;

import java.util.HashMap;
import java.util.Map;

import com.microserviceshack2.dictionary.Config;
import com.microserviceshack2.dictionary.Message;
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
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("User", "Ivan");
			data.put("Data", "Hello Hackers");

			Message message = new Message();
			message.setDetails(data);
			String string = message.getJson();

			channel.basicPublish(Config.EXCHANGE, routingKey, null,
					string.getBytes());
			System.out.println(" [x] Sent '" + routingKey + "':'" + data + "'");

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