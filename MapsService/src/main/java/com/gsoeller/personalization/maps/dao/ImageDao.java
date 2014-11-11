package com.gsoeller.personalization.maps.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;

public class ImageDao {

	private static final String OUTPUT_DIR = "/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/img/";

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
	
	public BufferedImage getImage(String name) {
		BufferedImage img;
		try {
			img = ImageIO.read(new File(OUTPUT_DIR + name));
			return img;
		} catch (IOException e) {
			throw new RuntimeException("An IOException occurred while reading an image");
		}
	}
	
	public void removeImage(String name) {
		try {
			Files.deleteIfExists(Paths.get(OUTPUT_DIR + name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
