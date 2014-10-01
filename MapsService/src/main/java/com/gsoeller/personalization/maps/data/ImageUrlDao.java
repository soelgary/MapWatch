package com.gsoeller.personalization.maps.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;


public class ImageUrlDao {

	private List<ImageUrl> images = Lists.newArrayList();
	
	public ImageUrlDao() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/google.txt"));
			//BufferedReader br = new BufferedReader(new FileReader("/home/soelgary/Maps/google.txt"));
			String line;
			while((line = br.readLine()) != null) {
				images.add(new ImageUrl(MapWebsite.GoogleMaps, new URL(MapWebsite.GoogleMaps.getBaseUrl() + line)));
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageUrl get(int index) {
		if(index >= images.size()) {
			throw new RuntimeException("Index too large");
		} else if(index < 0) {
			throw new RuntimeException("Index cannot be negative");
		}
		return images.get(index);
	}
	
	public int size() {
		return images.size();
	}
}
