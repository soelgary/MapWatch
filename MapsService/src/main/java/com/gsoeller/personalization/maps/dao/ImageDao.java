package com.gsoeller.personalization.maps.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;


import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;

import com.gsoeller.personalization.maps.PropertiesLoader;

public class ImageDao {

	private String outputDir;
	
	public ImageDao() {
		
		try {
			PropertiesLoader propLoader = new PropertiesLoader();
			outputDir = propLoader.getProperty("imgdirectory");
		} catch (IOException e) {
			e.printStackTrace();
			outputDir = "/net/data/google-maps/img/";
		}
		
	}
	
	public void saveImage(String filename, HttpEntity entity) {
		File imageFile = new File(outputDir + filename);
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
			img = ImageIO.read(new File(outputDir + name));
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("An IOException occurred while reading an image");
		}
	}
	
	public void removeImage(String name) {
		try {
			FileUtils.forceDelete(new File(outputDir + name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setDir(String dir) {
		this.outputDir = dir;
	}
}
