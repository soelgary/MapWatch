package com.gsoeller.personalization.maps.fetchers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.data.MapRequest;

public class StaticMapFetcher {
	
	private final CloseableHttpClient client = HttpClients.createDefault();

	private static final Logger LOG = LoggerFactory
			.getLogger(StaticMapFetcher.class);

	public Optional<HttpResponse> fetch(MapRequest request) {
		try {
			HttpResponse response = makeRequest(request, 3);
			return Optional.of(response);
		} catch(RuntimeException e) {
			return Optional.absent();
		}
	}
	
	private HttpResponse makeRequest(MapRequest request, int retries) {
		HttpGet httpGet = new HttpGet(request.buildRequestUrl());
		for(int i = 0; i < retries; i++) {
			try {
				CloseableHttpResponse response = client.execute(httpGet);
				if(response.getStatusLine().getStatusCode() == 200) {
					LOG.info(String.format("Successfully made a request to the Static Maps API on request attempt '%s' for request '%s'", i, request.buildRequestUrl()));
					return response;
				} else {
					LOG.warn(String.format("Request to Static Maps API Failed on attempt '%s' for request '%s'", i, request.buildRequestUrl()));
					LOG.warn(String.format("%s", response.getStatusLine().getReasonPhrase()));
				}
				
			} catch (ClientProtocolException e) {
				LOG.warn(String.format("Client Protocol Exception on attempt '%s' for request '%s'", i, request.buildRequestUrl()));
				e.printStackTrace();
			} catch (IOException e) {
				LOG.warn(String.format("IOException on attempt '%s' for request '%s'", i, request.buildRequestUrl()));
				e.printStackTrace();
			}
		}
		throw new RuntimeException(String.format("Ran out of attempts for request, '%s'", request.buildRequestUrl()));
	}
}
