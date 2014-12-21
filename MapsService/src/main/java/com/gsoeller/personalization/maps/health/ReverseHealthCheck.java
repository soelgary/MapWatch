package com.gsoeller.personalization.maps.health;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.data.MonitorRequest;

public class ReverseHealthCheck {
	
	private String host;
	
	private final CloseableHttpClient client;
	private ObjectMapper mapper = new ObjectMapper();
	
	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.heal.ReverseHealthCheck");
	
	public ReverseHealthCheck() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		host = propLoader.getProperty("healthhost");
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(10000)
				.setSocketTimeout(10000)
				.build();
		client = HttpClients.custom()
				.setDefaultRequestConfig(config)
				.build();
	}
	
	public void sendReverseHealthCheck(String mapProvider) throws UnsupportedEncodingException {
		CloseableHttpResponse response;
		try {
			HttpPost post = new HttpPost(host);
			long now = DateTime.now().getMillis();
			MonitorRequest monitorRequest = new MonitorRequest(mapProvider, now);
			String request = mapper.writeValueAsString(monitorRequest);
			StringEntity entity = new StringEntity(request);
			entity.setContentType("application/json");
			post.setEntity(entity);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 201) {
				LOG.info(String.format("Successfully made a request to the Reverse Health Checks API\n%s", request));
			} else {
				LOG.severe(String.format("%s", response.getStatusLine().getReasonPhrase()));
			}
			response.close();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
