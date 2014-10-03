package com.gsoeller.personalization.maps;

import org.apache.http.HttpEntity;
import com.gsoeller.personalization.maps.data.ImageDao;
import com.gsoeller.personalization.maps.data.ImageUrl;
import com.gsoeller.personalization.maps.data.ImageUrlDao;


public class App {
	public static void main(String[] args) {
		ImageUrlDao imageUrlDao = new ImageUrlDao();
		ImageDao imageDao = new ImageDao();
		System.out.println(imageUrlDao.size());

		//ImageUrl image1 = imageUrlDao.get(0);
		//FileFetcher fetcher = new FileFetcher();
		//HttpEntity entity = fetcher.getImage(image1);
		//imageDao.saveImage("imageApp.png", entity);
		
		StaticMapFetcher mapFetcher = new StaticMapFetcher();
		mapFetcher.fetch("temp");
		
	}
}
