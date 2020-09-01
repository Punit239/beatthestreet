package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IEXNewsRecord {

	@JsonProperty("datetime")
	private String dateTime;
	@JsonProperty("headline")
	private String headline;
	@JsonProperty("source")
	private String source;
	@JsonProperty("url")
	private String url;
//	@JsonProperty("summary")
//	private String summary;
//	@JsonProperty("related")
//	private String related;
//	@JsonProperty("image")
//	private String image;
	@JsonProperty("lang")
	private String lang;
//	@JsonProperty("hasPaywall")
//	private String hasPaywall;
}
