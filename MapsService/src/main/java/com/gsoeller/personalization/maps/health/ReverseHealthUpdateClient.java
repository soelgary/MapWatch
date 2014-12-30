package com.gsoeller.personalization.maps.health;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.data.TileUpdate;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ReverseHealthUpdateClient {

	private String host;

	private final CloseableHttpClient client;
	private ObjectMapper mapper = new ObjectMapper();

	private Logger LOG = MapsLogger.createLogger("com.gsoeller.personalization.maps.data.ReverseHealthUpdateClient");

	public ReverseHealthUpdateClient() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		host = propLoader.getProperty("healthhost");
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(10000)
				.setSocketTimeout(10000)
				.build();
		client = HttpClients.custom().setDefaultRequestConfig(config).build();
	}

	public void sendUpdate(TileUpdate update) {
		CloseableHttpResponse response;
		try {
			HttpPost post = new HttpPost(host);
			String request = mapper.writeValueAsString(update);
			StringEntity entity = new StringEntity(request);
			entity.setContentType("application/json");
			post.setEntity(entity);
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 201) {
				LOG.info(String.format("Successfully made a request to the Reverse Health Checks Update API\n%s",request));
			} else {
				LOG.severe(String.format("%s", response.getStatusLine().getReasonPhrase()));
			}
			response.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}