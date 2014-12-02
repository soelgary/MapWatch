package com.gsoeller.personalization.maps;

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
import com.gsoeller.personalization.maps.dao.ImageDao;
import com.gsoeller.personalization.maps.data.MapRequest;

public class StaticMapFetcher {
	private final String API_KEY = "AIzaSyCuoblPc_plcMWIOGi7C5Td2mRQccxkpuc";
	private final String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?center=26,78&zoom=5&size=600x600&sensor=true&region=cn&language=english&key="
			+ API_KEY;
	private final CloseableHttpClient client = HttpClients.createDefault();
	private final ImageDao imageDao = new ImageDao();

	private static final Logger LOG = LoggerFactory
			.getLogger(StaticMapFetcher.class);

	public void fetch(String filename) {
		HttpGet request = new HttpGet(STATIC_MAP_API_ENDPOINT);
		try {
			CloseableHttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				LOG.info("Successfully made a request to the Static Maps API");
				imageDao.saveImage(filename + ".png", response.getEntity());
			} else {
				LOG.error("Request to Static Maps API Failed");
				LOG.error(response.getStatusLine().getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Optional<HttpResponse> fetch(MapRequest request) {
		try {
			HttpResponse response = makeRequest(request, 3);
			return Optional.of(response);
		} catch(RuntimeException e) {
			return Optional.absent();
		}
	}
	
	public HttpResponse makeRequest(MapRequest request, int retries) {
		HttpGet httpGet = new HttpGet(request.buildRequestUrl());
		for(int i = 0; i < retries; i++) {
			try {
				CloseableHttpResponse response = client.execute(httpGet);
				if(response.getStatusLine().getStatusCode() == 200) {
					System.out.println(String.format("Successfully made a request to the Static Maps API on request attempt '%s' for request '%s'", i, request.buildRequestUrl()));
					return response;
				} else {
					System.out.println(String.format("Request to Static Maps API Failed on attempt '%s' for request '%s'", i, request.buildRequestUrl()));
					System.out.println(String.format("%s", response.getStatusLine().getReasonPhrase()));
				}
				
			} catch (ClientProtocolException e) {
				System.out.println(String.format("Client Protocol Exception on attempt '%s' for request '%s'", i, request.buildRequestUrl()));
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(String.format("IOException on attempt '%s' for request '%s'", i, request.buildRequestUrl()));
				e.printStackTrace();
			}
		}
		throw new RuntimeException(String.format("Ran out of attempts for request, '%s'", request.buildRequestUrl()));
	}
}
