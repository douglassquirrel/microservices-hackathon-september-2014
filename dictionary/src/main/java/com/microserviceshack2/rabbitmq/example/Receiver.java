package com.microserviceshack2.rabbitmq.example;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceshack2.dictionary.Config;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Receiver {

	public static void main(String[] argv) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Config.RABBIT_MQ_SERVER);

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Config.EXCHANGE, "topic");
			String queueName = channel.queueDeclare().getQueue();

			String bindingKey = "topic";
			channel.queueBind(queueName, Config.EXCHANGE, bindingKey);

			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				String routingKey = delivery.getEnvelope().getRoutingKey();

				System.out.println(" [x] Received '" + routingKey + "':'"
						+ message + "'");

				try {
					ObjectMapper mapper = new ObjectMapper();
					@SuppressWarnings("unchecked")
					Map<Object, Object> response = mapper.readValue(message,
							Map.class);
					for (Object key : response.keySet()) {
						System.out.println("Key is " + key + " value is "
								+ response.get(key));
					}
				} catch (Exception e) {
				}
			}
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