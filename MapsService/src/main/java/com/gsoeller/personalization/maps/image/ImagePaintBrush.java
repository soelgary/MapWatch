package com.gsoeller.personalization.maps.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagePaintBrush {
	
	private static final int CROP_AMOUNT = 40;
	
	/*
	 * Takes an image, text, and filename and draws the text on the image
	 * and outputs it to the filename provided
	 * 
	 */
	public static BufferedImage draw(BufferedImage image, String text, String output) throws IOException {
		int height = image.getHeight();
		int width = image.getWidth();
		BufferedImage bufferedImage = new BufferedImage(width, height + 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height+100);
		g.drawImage(image, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString(text, 50, 650);
		g.dispose();
		return bufferedImage;
	}
	
	/*
	 * Takes an image and crops the bottom out and saves it to cropped.png
	 */
	public static BufferedImage crop(BufferedImage image) {
		return image.getSubimage(0, 0, image.getWidth(), image.getHeight() - CROP_AMOUNT);
	}
}