package com.gsoeller.personalization.maps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MapsLogger {

	public static Logger createLogger(String className) {
		Logger logger = Logger.getLogger(className);
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream("/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/mylogging.properties"));
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
