package com.microserviceshack2.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Receiver {
	String createdUrl = null;
	String updatedUrl = null;

	public String getQueueUrl(String queueName) {
		HttpProcessor httpproc = HttpProcessorBuilder.create()
				.add(new RequestContent()).add(new RequestTargetHost())
				.add(new RequestConnControl())
				.add(new RequestUserAgent("Test/1.1"))
				.add(new RequestExpectContinue(true)).build();

		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		HttpCoreContext coreContext = HttpCoreContext.create();
		HttpHost host = new HttpHost(Config.REST_HOST, Config.REST_PORT);
		coreContext.setTargetHost(host);

		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(
				8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

		Message message = new Message();
		try {

			HttpEntity[] requestBodies = { new StringEntity("",
					ContentType.create("text/plain", Consts.UTF_8)) };

			for (int i = 0; i < requestBodies.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(),
							host.getPort());
					conn.bind(socket);
				}
				BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest(
						"POST", "/topics/" + queueName + "/queue");
				request.setEntity(requestBodies[i]);
				System.out.println(">> Request URI: "
						+ request.getRequestLine().getUri());

				httpexecutor.preProcess(request, httpproc, coreContext);
				HttpResponse response = httpexecutor.execute(request, conn,
						coreContext);
				httpexecutor.postProcess(response, httpproc, coreContext);

				System.out.println("<< Response: " + response.getStatusLine());
				message.setFromJson(EntityUtils.toString(response.getEntity()));
				message.dump();
				// System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("==============");
				if (!connStrategy.keepAlive(response, coreContext)) {
					conn.close();
				} else {
					System.out.println("Connection kept alive...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return (String) message.get("queue_url");
	}

	private Object getMessageFromQueue(String url) throws JsonParseException,
			JsonMappingException, IOException {
		String json = getHTML(url);
		Message message = new Message();
		message.setFromJson(json);
		System.out.println("Received message");
		message.dump();
		return message.getDetails();
	}

	private String getHTML(String urlToRead) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void poll() {

		while (true) {
			Object data = getNextMessage();
			if(data == null) {
				continue;
			}
			String id = ((Map)data).get("id").toString();
			List<List<String>> grid  = (List<List<String>>)(((Map)data).get("grid"));
			List<String> gridConverted = new ArrayList<String>();
			for (List<String> list : grid) {
				StringBuilder sb = new StringBuilder();
				for (String string : list) {
					sb.append(string);
				}
				gridConverted.add(sb.toString());
			}
		}
	}

	public Object getNextMessage() {
		Object data = null;
		try {
			data = getMessageFromQueue(createdUrl);
		} catch (Exception e) {
			try {
				data = getMessageFromQueue(updatedUrl);
			} catch (Exception e1) {
			}
		}

		return data;
	}

	public List<String> getColumns(List<String> rows) {
		List<String> columns = new ArrayList<String>();
		if (!rows.isEmpty()) {
			for (int i = 0; i < rows.get(0).length(); i++) {
				StringBuilder column = new StringBuilder();
				for (String row : rows) {
					column.append(row.charAt(i));
				}
				columns.add(column.toString());
			}
		}
		return columns;

	}

	private void init() {
		createdUrl = getQueueUrl("board.created");
		updatedUrl = getQueueUrl("board.updated");

		System.out.println("URL is " + createdUrl);
		System.out.println("URL is " + updatedUrl);
	}

	public static void main(String args[]) {
		Receiver receiver = new Receiver();
		receiver.init();
		receiver.poll();

	}
}
