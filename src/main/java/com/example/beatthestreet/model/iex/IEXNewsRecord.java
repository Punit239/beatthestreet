package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	public String getDateTime() {
		return dateTime;
	}
	public String getHeadline() {
		return headline;
	}
	public String getSource() {
		return source;
	}
	public String getUrl() {
		return url;
	}
//	public String getSummary() {
//		return summary;
//	}
//	public String getRelated() {
//		return related;
//	}
//	public String getImage() {
//		return image;
//	}
	public String getLang() {
		return lang;
	}
//	public String getHasPaywall() {
//		return hasPaywall;
//	}
}
