package com.gsoeller.personalization.maps.ocr;

import java.awt.image.BufferedImage;

import com.google.common.base.Optional;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TextDetector {

	Tesseract instance = Tesseract.getInstance();
	
	public Optional<String> findText(BufferedImage image) {
		try {
			String text = instance.doOCR(image);
			if(!text.isEmpty()) {
				return Optional.of(text);
			}
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return Optional.absent();
	}
}
