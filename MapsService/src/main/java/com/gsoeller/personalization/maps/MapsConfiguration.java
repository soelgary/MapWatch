package com.gsoeller.personalization.maps;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class MapsConfiguration extends Configuration {

	@Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    
    @NotEmpty
    private String jwtTokenSecret = "dfwzsdzwh823zebdwdz772632gdsbd";

    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return jwtTokenSecret.getBytes("UTF-8");
    }
    
    private String mapProvider;
	
	@JsonProperty("mapProvider")
	public String getMapProvider() {
		return mapProvider;
	}
	
	@JsonProperty("mapProvider")
	public void setMapProvider(String mapProvider) {
		this.mapProvider = mapProvider;
	}
	
	private int fetchJob;
		
	@JsonProperty("fetchJob")
	public int getFetchJob() {
		return fetchJob;
	}
		
	@JsonProperty("fetchJob")
	public void setFetchJob(int fetchJob) {
		this.fetchJob = fetchJob;
	}
}
