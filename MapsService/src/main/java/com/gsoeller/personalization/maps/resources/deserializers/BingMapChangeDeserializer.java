package com.gsoeller.personalization.maps.resources.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.gsoeller.personalization.maps.data.BingMap;
import com.gsoeller.personalization.maps.data.Map;

public class BingMapChangeDeserializer extends JsonDeserializer<Map>{

	@Override
	public Map deserialize(JsonParser jp, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		int id = (Integer) ((IntNode) node.get("id")).intValue();
		return new BingMap.MapBuilder().setId(id).build();
	}
}
