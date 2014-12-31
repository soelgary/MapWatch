package com.gsoeller.personalization.maps.image;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cafe.image.gif.GIFTweaker;
import cafe.util.ArrayUtils;

import com.google.common.collect.Lists;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.ImageDao;
import com.gsoeller.personalization.maps.data.Region;

public class GifGenerator {

	private BufferedImage[] images;
	private int[] delays;
	private FileOutputStream fout;
	private List<String> countries;
	private int[] fetchJobs;
	private int mapRequest;
	private String gifDirectory;
	
	private GifGenerator(BufferedImage[] images, int[] delays, FileOutputStream fout, List<String> countries, int[] fetchJobs, int mapRequest, String gifDirectory) {
		this.images = images;
		this.delays = delays;
		this.fout = fout;
		this.countries = countries;
		this.fetchJobs = fetchJobs;
		this.mapRequest = mapRequest;
		this.gifDirectory = gifDirectory;
	}
	
	public void createGif() throws Exception {
		List<String> outputFiles = Lists.newArrayList();
		BufferedImage[] bufferedImages = new BufferedImage[images.length];
		for(int i = 0; i < images.length; i++) {
			String output = String.format("%s-%d-%d", countries.get(i), fetchJobs[i], mapRequest);
			String outputGif = String.format("%s%s.gif", gifDirectory, output);
			bufferedImages[i] = ImagePaintBrush.draw(images[i], output, outputGif);
			outputFiles.add(outputGif);
		}
		GIFTweaker.writeAnimatedGIF(bufferedImages, delays, fout);
		
	}
	
	public static class GifGeneratorBuilder {
		private List<BufferedImage> images = Lists.newArrayList();
		private List<Integer> delays = Lists.newArrayList();
		private List<String> countries = Lists.newArrayList();
		private List<Integer> fetchJobs = Lists.newArrayList();
		private FileOutputStream fout;
		private final int DEFAULT_DELAY = 1000;
		private String gifDirectory;
		private String gifName;
		private int mapRequest;
		
		private ImageDao imageDao = new ImageDao();
		
		public GifGeneratorBuilder() throws IOException {
			PropertiesLoader propLoader = new PropertiesLoader();
			gifDirectory = propLoader.getProperty("gifdirectory");
			gifName = UUID.randomUUID().toString() + "gif";
		}
		
		public GifGeneratorBuilder setMapRequest(int mapRequest) {
			this.mapRequest = mapRequest;
			return this;
		}
		
		public GifGeneratorBuilder setFilename(String gifName) {
			this.gifName = gifName;
			return this;
		}
		
		public GifGeneratorBuilder addImage(String path, int delay, List<String> countries) {
			images.add(imageDao.getImage(path));
			delays.add(delay);
			this.countries = countries;
			return this;
		}
		
		public GifGeneratorBuilder addImage(String path, Region country, int fetchJob) {
			images.add(imageDao.getImage(path));
			delays.add(DEFAULT_DELAY);
			fetchJobs.add(fetchJob);
			countries.add(country.toString());
			return this;
		}
		
		public GifGeneratorBuilder setGifDirectory(String gifDirectory) {
			this.gifDirectory = gifDirectory;
			return this;
		}
		
		public GifGenerator build() throws FileNotFoundException {
			BufferedImage[] images = this.images.toArray(new BufferedImage[this.images.size()]);
			int[] delays = ArrayUtils.toPrimitive(this.delays.toArray(new Integer[this.delays.size()]));
			int[] fetchJobs = ArrayUtils.toPrimitive(this.fetchJobs.toArray(new Integer[this.fetchJobs.size()]));
			fout = new FileOutputStream(gifDirectory + gifName);
			return new GifGenerator(images, delays, fout, countries, fetchJobs, mapRequest, gifDirectory);
		}
	}
}