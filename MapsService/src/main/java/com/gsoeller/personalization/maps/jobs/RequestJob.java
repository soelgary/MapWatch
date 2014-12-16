package com.gsoeller.personalization.maps.jobs;

import java.io.IOException;

import io.dropwizard.jdbi.OptionalContainerFactory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.MapRequestDao;

public class RequestJob implements Job {

	private final int startLat = -60;
	private final int startLon = -180;
	private final int maxLat = 75;
	private final int maxLon = 180;
	private final int latMultiple = 8;
	private final int lonMultiple = 10;
	private final String[] CC_TLD = {"ac", "ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", 
	          "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cr", "cs", "cu", "cv", "cw", "cx", "cy", "cz", "dd", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "er", "es", "et", "eu", "fi", "fj",
	          "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "im", "in", "io", "iq", "ir",
	          "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "me", "mg", "mh", "mk", "ml", "mm",
	          "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "ps",
	          "pt", "pw", "py", "qa", "re", "ro", "rs", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "ss", "st", "su", "sv", "sx", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj",
	          "tk", "tl", "tm", "tn", "to", "tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "yu", "za", "zm", "zr", "zw"};
	//private final List<Box> boxes = Lists.newArrayList(new Box(-160, -84, -60, -4), 
	//		new Box(-180, -135, 34, 50),
	//		new Box(-66, -19, 26, 40),
	//		new Box(-27, 3, -66, -2),
	//		new Box(66, 94, -60 ,-3));
	//private final String[] CC_TLD = {"en", "in"};
	
	private DBI dbi;
	private Handle handle;
	private MapRequestDao mapRequestDao;

	private static final Logger LOG = LoggerFactory.getLogger(FetchJob.class);

	public RequestJob() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		mapRequestDao = handle.attach(MapRequestDao.class);
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int mapNumber = (Integer)context.getJobDetail().getJobDataMap().get("mapNumber");
		for(int lat = startLat; lat < maxLat; lat+=latMultiple) {
			for(int lon = startLon; lon < maxLon; lon+= lonMultiple) {
				//int location = locationDao.addLocation(lat, lon, mapNumber);
				if(!inBox(lat, lon)) {
					System.out.println(lat + "\t" + lon);
					for(String cc: CC_TLD) {
						mapRequestDao.addMapRequest(mapNumber, lat, lon, 6, 600, 600, cc, "English");
					}
				}
			}
		}
		System.exit(0);
	}
	
	public boolean inBox(double lat, double lon) {
		/*
		for(Box box: boxes) {
			if(box.inBox(lat, lon)) {
				return true;
			}
		}
		*/
		return false;
	}

}
