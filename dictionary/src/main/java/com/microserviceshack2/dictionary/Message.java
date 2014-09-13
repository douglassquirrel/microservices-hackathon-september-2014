package com.microserviceshack2.dictionary;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
	private Map<String, Object> details;

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	public String getJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(details);
	}

	@SuppressWarnings("unchecked")
	public void setFromJson(String json) throws JsonParseException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		this.details = mapper.readValue(json, Map.class);

	}

	public Object get(String key) {
		return details.get(key);
	}

	public void dump() {
		for (Object key : details.keySet()) {
			System.out.println("Key is " + key + " value is "
					+ details.get(key));
		}

	}
}
