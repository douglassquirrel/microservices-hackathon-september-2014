package com.microserviceshack2.dictionary;


public class Config {

	/*
	 * "pg_host":
	 * "microservices-chris-10sep2014.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com",
	 * "pg_database": "micro", "pg_user": "microservices", "pg_password":
	 * "microservices", "web_host": "0.0.0.0", "web_port": 8080, "web_url":
	 * "http://54.76.117.95:8080"
	 */

	// Rabbit MQ Messaging

	/**
	 * "rabbit_host": "54.76.117.95", "rabbit_port": 5672,
	 */
	public static final String RABBIT_MQ_SERVER = "54.76.117.95";
	public static final int RABBIT_MQ_PORT = 5672;
	// public static final String TASK_QUEUE_NAME = "gonka"; // "task_queue";
	public static final String EXCHANGE = "combo";

	// Postgres Database

	/*
	 * "pg_host":
	 * "microservices-chris-10sep2014.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com",
	 * "pg_database": "micro", "pg_user": "microservices", "pg_password":
	 * "microservices",
	 */
	public static final String POSTGRES_HOST = "microservices-chris-10sep2014.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com";
	public static final String POSTGRES_DATABASE = "micro";
	public static final String POSTGRES_USER = "microservices";
	public static final String POSTGRES_PASSWORD = "microservices";

	// REST

	public static final String REST_HOST = "54.76.117.95";
	public static final int REST_PORT = 8080;

	public static final String REST_URL = "http://" + REST_HOST + ":"
			+ REST_PORT;

}
