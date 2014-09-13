package com.microserviceshack2.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Connection {

	public String getTopics(String url, String path) {
		Client client = ClientBuilder.newClient();
		String name = client.target(url + "/" + path)
				.request(MediaType.TEXT_PLAIN).get(String.class);

		return name;
	}

	public int postTopic(String url, String path, String topicName,
			String fromId) {
		Client client = ClientBuilder.newClient();
		Response response = client.target(url + "/" + path + "/" + topicName)
				.request(MediaType.TEXT_PLAIN).post(null);
		return response.getStatus();
	}

	public int postTopicWithBody(String url, String path, String topicName,
			WordsFound wordFound) {
		Client client = ClientBuilder.newClient();
		String json = wordFound.toJson();
		Entity<String> entity = Entity.entity(json,
				MediaType.APPLICATION_JSON_TYPE);
		Response response = client.target(url + "/" + path + "/" + topicName)
				.request(MediaType.APPLICATION_JSON).post(entity);
		return response.getStatus();
	}

}