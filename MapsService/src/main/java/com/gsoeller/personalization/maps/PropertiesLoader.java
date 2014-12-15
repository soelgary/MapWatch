package com.gsoeller.personalization.maps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	private Properties prop = new Properties();
	private InputStream input = null;

	public PropertiesLoader(String configFile) throws IOException {
		input = new FileInputStream(configFile);
		prop.load(input);
	}
	
	public PropertiesLoader() throws IOException {
		//input = new FileInputStream("/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/maps.properties");
		input = new FileInputStream("/home/soelgary/Maps/achtung.properties");
		prop.load(input);
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}
}
