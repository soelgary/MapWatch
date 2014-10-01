package com.gsoeller.personalization.maps;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.gsoeller.personalization.maps.data.ImageDao;

public class StaticMapFetcher {
	private static final String API_KEY =  "AIzaSyCuoblPc_plcMWIOGi7C5Td2mRQccxkpuc";
	private static final String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?center=26,78&zoom=5&size=600x600&sensor=true&region=en&language=english&key=" + API_KEY;
	private final CloseableHttpClient client = HttpClients.createDefault();
	private final ImageDao imageDao = new ImageDao();
	
	
	private static final Logger LOG = LoggerFactory.getLogger(StaticMapFetcher.class);
	
	
	public void fetch() {
		HttpGet request  = new HttpGet(STATIC_MAP_API_ENDPOINT);
		try {
			CloseableHttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200) {
				LOG.info("Successfully made a request to the Static Maps API");
				imageDao.saveImage("temp1.png", response.getEntity());
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
}
