package com.gsoeller.personalization.maps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MapsLogger {

	public static Logger createLogger(String className) {
		Logger logger = Logger.getLogger(className);
		try {
			FileInputStream inputStream = new FileInputStream(PropertiesLoader.getProperty("logproperties"));
			LogManager.getLogManager().readConfiguration(inputStream);
			//FileHandler fileHandler = new FileHandler(loader.getProperty("logpath") + "logs1.log");
			//fileHandler.setLevel(Level.ALL);
			logger.setLevel(Level.ALL);
			//logger.addHandler(fileHandler);
			inputStream.close();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return logger;
	}
}
