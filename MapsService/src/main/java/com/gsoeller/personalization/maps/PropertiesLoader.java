package com.gsoeller.personalization.maps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	public static String getProperty(String key) throws IOException {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/maps.properties");
		} catch (IOException e) {
			input = new FileInputStream("/home/soelgary/dev/src/MapsPersonalization/MapsService/src/main/resources/achtung.properties");
		}
		prop.load(input);
		input.close();
		return prop.getProperty(key);
	}
}
