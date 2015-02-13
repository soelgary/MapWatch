package com.gsoeller.personalization.maps.ocr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.dao.ImageDao;

@RunWith(JUnit4.class)
public class TestTextDetector {
	
	private final TextDetector detector = new TextDetector();
	
	private ImageDao imageDao = new ImageDao();
	private final String IMG_LOCATION = "/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/test/img/";
	
	
	@Before
	public void setup() {
		imageDao.setDir(IMG_LOCATION);
	}
	
	
	@Test
	public void testSameImage() {
		String imgName = "diff.png";
		BufferedImage image = imageDao.getImage(imgName);
		Optional<String> text = detector.findText(image);
		assertFalse(text.isPresent());
	}
	
	@Test
	public void testImageWithText() {
		String imgName = "yep1.png";
		BufferedImage image = imageDao.getImage(imgName);
		Optional<String> text = detector.findText(image);
		assertTrue(text.isPresent());
	}
}
