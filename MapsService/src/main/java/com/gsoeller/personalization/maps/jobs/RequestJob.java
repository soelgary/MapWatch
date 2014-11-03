package com.gsoeller.personalization.maps.jobs;

import io.dropwizard.jdbi.OptionalContainerFactory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gsoeller.personalization.maps.dao.LocationDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;

public class RequestJob implements Job {

	private final int startLat = -60;
	private final int startLon = -180;
	private final int maxLat = 75;
	private final int maxLon = 180;
	private final int latMultiple = 8;
	private final int lonMultiple = 15;
	private final String[] CC_TLD = {"ac", "ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", 
	          "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cr", "cs", "cu", "cv", "cw", "cx", "cy", "cz", "dd", "de", "dj", "dk", "dm", /*"do",*/ "dz", "ec", "ee", "eg", "eh", "er", "es", "et", "eu", "fi", "fj",
	          "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "im", "in", "io", "iq", "ir",
	          "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "me", "mg", "mh", "mk", "ml", "mm",
	          "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "ps",
	          "pt", "pw", "py", "qa", "re", "ro", "rs", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "ss", "st", "su", "sv", "sx", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj",
	          "tk", "tl", "tm", "tn", "to", "tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "yu", "za", "zm", "zr", "zw"};
	
	
	private DBI dbi;
	private Handle handle;
	private MapRequestDao mapRequestDao;
	private LocationDao locationDao;

	private static final Logger LOG = LoggerFactory.getLogger(FetchJob.class);

	public RequestJob() {
		dbi = new DBI("jdbc:mysql://localhost/Maps", "root", "");
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		mapRequestDao = handle.attach(MapRequestDao.class);
		locationDao = handle.attach(LocationDao.class);

	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("vwevw");
		for(int lat = startLat; lat < maxLat; lat+=latMultiple) {
			for(int lon = startLon; lon < maxLon; lon+= lonMultiple) {
				System.out.println(lat + "\t" + lon);
				int location = locationDao.addLocation(lat, lon);
				for(String cc: CC_TLD) {
					mapRequestDao.addMapRequest(location, 6, 600, 600, cc, "English");
				}
			}
		}
		System.exit(0);
	}

}
