package com.gsoeller.personalization.maps.fetchers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.data.MapRequest;

public class StaticMapFetcher {
	
	//private final CloseableHttpClient client = HttpClients.createDefault();

	private static final Logger LOG = LoggerFactory
			.getLogger(StaticMapFetcher.class);
	
	private String outputDir;
	
	public StaticMapFetcher() {
		try {
			outputDir = PropertiesLoader.getProperty("imgdirectory");
		} catch (IOException e) {
			e.printStackTrace();
			outputDir = "/net/data/google-maps/img/";
		}
	}

	public Optional<String> fetch(MapRequest request) {
		try {
			//HttpResponse response = makeRequest(request, 3);
			//return Optional.of(response);
			return saveImage(request);
		} catch(RuntimeException e) {
			return Optional.absent();
		}
	}
	

	public Optional<String> saveImage(MapRequest request) {
		String filename = UUID.randomUUID().toString() + ".png";
		try {
			URL url = new URL(request.buildRequestUrl());
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(outputDir + filename);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
			return Optional.fromNullable(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.absent();
	}
	
	/*
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
	*/
}
