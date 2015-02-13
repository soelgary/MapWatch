package com.gsoeller.personalization.maps.jobs;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.im4java.core.IM4JavaException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.ImageDao;
import com.gsoeller.personalization.maps.image.ImageMagic;
import com.gsoeller.personalization.maps.image.ImagePaintBrush;

public class Cropjob implements Job{
	
	PropertiesLoader propLoader;
	
	public Cropjob() throws IOException {
		propLoader = new PropertiesLoader();
	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ImageDao imageDao = new ImageDao();
		BufferedImage image = imageDao.getImage(propLoader.getProperty("cropimage"));
		//ImagePaintBrush.crop(image);
		try {
			ImageMagic magic = new ImageMagic();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.exit(0);
	}
}
