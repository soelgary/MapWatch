package com.gsoeller.personalization.maps;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.gsoeller.personalization.maps.data.ImageUrl;

public class FileFetcher {

	private final CloseableHttpClient httpclient = HttpClients.createDefault();

	public HttpEntity getImage(ImageUrl imageUrl) {
		HttpGet httpget = new HttpGet(imageUrl.getUrl().toString());
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			return response.getEntity();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException("An error occurred and could not fetch the requested image");
	}
}
