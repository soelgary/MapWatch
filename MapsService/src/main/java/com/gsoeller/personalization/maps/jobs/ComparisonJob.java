package com.gsoeller.personalization.maps.jobs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import io.dropwizard.jdbi.OptionalContainerFactory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.gsoeller.personalization.maps.MapsLogger;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.dao.BingFetchJobDao;
import com.gsoeller.personalization.maps.dao.BingMapDao;
import com.gsoeller.personalization.maps.dao.BingMapRequestDao;
import com.gsoeller.personalization.maps.dao.FetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleFetchJobDao;
import com.gsoeller.personalization.maps.dao.GoogleMapDao;
import com.gsoeller.personalization.maps.dao.GoogleMapRequestDao;
import com.gsoeller.personalization.maps.dao.MapDao;
import com.gsoeller.personalization.maps.dao.MapRequestDao;
import com.gsoeller.personalization.maps.data.BingTile;
import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.GoogleFetchJob;
import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.data.Region;
import com.gsoeller.personalization.maps.data.GoogleTile;
import com.gsoeller.personalization.maps.smtp.MapsEmail;
import com.gsoeller.personalization.maps.smtp.MapsEmail.MapsEmailBuilder;
import com.gsoeller.personalization.maps.smtp.SmtpClient;

public class ComparisonJob implements Job {

	private DBI dbi;
	private Handle handle;
	private FetchJobDao fetchJobDao;
	private MapDao mapDao;
	private MapRequestDao mapRequestDao;
	private SmtpClient smtpClient = new SmtpClient();

	private Logger LOG = MapsLogger
			.createLogger("com.gsoeller.personalization.maps.jobs.ComparisonJob");

	public ComparisonJob() throws IOException {
		dbi = new DBI(PropertiesLoader.getProperty("db"),
				PropertiesLoader.getProperty("dbuser"),
				PropertiesLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
	}

	public boolean configure(String mapProvider) {
		if (mapProvider.equals("google")) {
			configureGoogle();
		} else if (mapProvider.equals("bing")) {
			configureBing();
		} else {
			return false;
		}
		return true;
	}

	public void configureBing() {
		fetchJobDao = handle.attach(BingFetchJobDao.class);
		mapDao = handle.attach(BingMapDao.class);
		mapRequestDao = handle.attach(BingMapRequestDao.class);
	}

	public void configureGoogle() {
		fetchJobDao = handle.attach(GoogleFetchJobDao.class);
		mapDao = handle.attach(GoogleMapDao.class);
		mapRequestDao = handle.attach(GoogleMapRequestDao.class);
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		LOG.info("Running comparisons");
		String mapProvider = (String) context.getJobDetail().getJobDataMap()
				.get("mapProvider");
		boolean configured = configure(mapProvider);
		if (!configured) {
			LOG.severe(String.format(
					"Does not recognize configuration for map provider, '%s'",
					mapProvider));
			System.exit(0);
		} else {
			LOG.info(String
					.format("Successfully configured comparison job for map provider, '%s'",
							mapProvider));
		}
		try {
			int fetchJob = (Integer) context.getJobDetail().getJobDataMap()
					.get("fetchJob");
			if (mapProvider.equals("bing")) {
				compareBing(fetchJob);
			} else if (mapProvider.equals("google")) {
				compareGoogle(fetchJob);
			} else {
				LOG.severe(String.format("Unknown map provider, '%s'",
						mapProvider));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void compareBing(int fetchJob) throws IOException {
		List<Integer> tileNumbers = mapRequestDao.getTileNumbers();

		for (int tileNumber : tileNumbers) {
			List<Integer> mapRequestIds = mapRequestDao
					.getMapRequestsFromTileNumber(tileNumber);
			BingTile tile = new BingTile(fetchJob);
			tile.setTile(tileNumber);
			for (Integer mapRequestId : mapRequestIds) {
				tile.addMap(mapRequestDao.getMapFromFetchJobAndMapRequest(
						fetchJob, mapRequestId));
			}
			tile.compare();
		}
		LOG.info("Finished running comparisons");
	}

	public void compareGoogle(int fetchJob) throws IOException {
		// need to get # of locations
		int numLocations = mapRequestDao.countTiles();
		System.out.println(numLocations);
		System.exit(0);
		HashMap<Integer, List<Integer>> locationToMapRequests = Maps.newHashMap(); // key -> location id, value -> list of map requests with that location
		// need to query map requests by location
		for(int i = 1; i <= numLocations; i++) {
			locationToMapRequests.put(i, mapRequestDao.getMapRequestsbyLocation(i));
		}
		// need to query for all maps that are the same tile
		for (Integer location : locationToMapRequests.keySet()) {
			List<Integer> mapRequests = locationToMapRequests.get(location);
			GoogleTile tile = new GoogleTile(fetchJob);
			for (Integer mapRequest : mapRequests) {
				// need to get map from fetch job with map request id
				Optional<Map> maps = mapDao.getMapFromFetchJobByMapRequest(fetchJob, mapRequest);
				if (!maps.isPresent()) {
					LOG.severe(String.format("Could not find any maps with fetchJob: %d, and mapRequest: %d", fetchJob, mapRequest));
				} else {
					tile.addMap(maps.get());
				}
			}
			// need to compare all tiles
			tile.compare();
		}
		LOG.info("Finished running comparisons");
	}

	public boolean canCompare(List<GoogleFetchJob> fetchJobs) {
		if (fetchJobs.size() < 2) {
			LOG.severe("Cannot compare because there are not 2 or more fetch jobs");
			return false;
		}

		return true;
	}

	/*
	 * public void execute(JobExecutionContext context) throws
	 * JobExecutionException { System.out.println("Running comparisons");
	 * Optional<Integer> mostRecentFetchJob = getMostRecentFetchJob();
	 * Optional<Integer> previousFetchJob = getPreviousFetchJob();
	 * if(mostRecentFetchJob.isPresent()) {
	 * compareFetchJob(mostRecentFetchJob.get()); } else {
	 * System.out.println("No jobs have been run yet"); System.exit(0); }
	 * 
	 * System.exit(0); }
	 */

	/*
	 * public void compareFetchJob(int fetchJob) { // is there any difference
	 * between countries? List<Map> maps =
	 * mapDao.getMaps();//mapDao.getMapsFromFetchJob(fetchJob); // need to check
	 * hash of images
	 * 
	 * // key -> hash ---- should probably be changes to MapRequest // value ->
	 * map // will show which maps have the same image and which ones do not
	 * Multimap<String, Map> mapHashes = HashMultimap.create();; for(Map map:
	 * maps) { mapHashes.put(map.getHash(), map); }
	 * 
	 * // is there only 1 key? // no personalization..
	 * if(mapHashes.keySet().size() < 2) {
	 * System.out.println("No personalization for this map"); } else {
	 * System.out.println("Personalization has been found"); String emailBody =
	 * String.format("Hash\t\t\t\t\t\t\t\tPath\n"); for(String hash:
	 * mapHashes.asMap().keySet()) { emailBody += String.format("%s\t%s\n",
	 * hash, mapHashes.get(hash).iterator().next().getPath()); } try { MapsEmail
	 * email = new MapsEmailBuilder()
	 * .setSubject("Personalization Between Countries") .setmMessage(emailBody)
	 * .addTo("mapspersonalization@gmail.com") .build();
	 * smtpClient.sendEmail(email); } catch(IOException e) {
	 * System.out.println("Cannot read properties file"); e.printStackTrace(); }
	 * } }
	 */

	/*
	 * public HashMap<Region, Integer> pairRegionWithMapRequest() {
	 * HashMap<Region, Integer> regions = Maps.newHashMap(); for(Region region:
	 * Lists.newArrayList(Region.en)){//Region.values()) { regions.put(region,
	 * mapRequestDao.getRequestByRegion(region.toString()).iterator().next()); }
	 * return regions; }
	 */

	/*
	 * public void compareFetchJobs(int fetchJob1, int fetchJob2) { // is there
	 * any difference between the two given fetch jobs? HashMap<Region, Integer>
	 * regions = pairRegionWithMapRequest(); for(Region region:
	 * Lists.newArrayList(Region.en)) {//Region.values()) { List<Map> job1Map =
	 * mapDao.getMapsFromFetchJobAndRegion(fetchJob1, regions.get(region));
	 * List<Map> job2Map = mapDao.getMapsFromFetchJobAndRegion(fetchJob2,
	 * regions.get(region)); if(job1Map.size() == job2Map.size() &&
	 * job2Map.size() == 1) { Map map1 = job1Map.get(0); Map map2 =
	 * job2Map.get(0); if (!map1.getHash().equals(map2.getHash())) {
	 * System.out.println("PERSONALIZATION"); } } else {
	 * System.out.println("Does not know what to compare"); } } }
	 */
	/*
	 * public Optional<Integer> getMostRecentFetchJob() { List<Integer>
	 * fetchJobs = fetchJobDao.getFetchJobs(); if(fetchJobs.isEmpty()) { return
	 * Optional.absent(); } return Optional.fromNullable(fetchJobs.get(0)); }
	 * 
	 * public Optional<Integer> getPreviousFetchJob() { List<Integer> fetchJobs
	 * = fetchJobDao.getFetchJobs(); if(fetchJobs.isEmpty() || fetchJobs.size()
	 * < 2) { return Optional.absent(); } return
	 * Optional.fromNullable(fetchJobs.get(1)); }
	 */
}