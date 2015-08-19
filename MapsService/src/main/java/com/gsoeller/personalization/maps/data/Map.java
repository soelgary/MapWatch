package com.gsoeller.personalization.maps.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gsoeller.personalization.maps.resources.deserializers.MapChangeDeserializer;

@JsonDeserialize(using = MapChangeDeserializer.class)
public interface Map {
	
	public String getPath();
	
	public String getHash();
	
	public MapRequest getMapRequest();
	
	public int getId();
}
