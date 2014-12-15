package com.gsoeller.personalization.maps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MapsLogger {

	public static Logger createLogger(String className) {
		Logger logger = Logger.getLogger(className);
		try {
			PropertiesLoader loader = new PropertiesLoader();
			LogManager.getLogManager().readConfiguration(new FileInputStream(loader.getProperty("logproperties")));
			//FileHandler fileHandler = new FileHandler(loader.getProperty("logpath") + "logs1.log");
			//fileHandler.setLevel(Level.ALL);
			logger.setLevel(Level.ALL);
			//logger.addHandler(fileHandler);
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
