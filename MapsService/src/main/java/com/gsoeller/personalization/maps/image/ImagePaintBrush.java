package com.gsoeller.personalization.maps.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImagePaintBrush {
	
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
		//ImageIO.write(bufferedImage, "png", new File(output));
		return bufferedImage;
	}
}
