package com.gsoeller.personalization.maps.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;

public class ImageDao {

	private static final String OUTPUT_DIR = "/Users/garysoeller/dev/src/maps/src/main/resources/";

	public void saveImage(String filename, HttpEntity entity) {
		File imageFile = new File(OUTPUT_DIR + filename);
		try {
			FileOutputStream foutStream = new FileOutputStream(imageFile);
			entity.writeTo(foutStream);
			foutStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
