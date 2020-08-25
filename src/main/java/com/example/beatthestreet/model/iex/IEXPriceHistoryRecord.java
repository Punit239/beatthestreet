package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IEXPriceHistoryRecord {

	@JsonProperty(value = "date")
	private String date;
	@JsonProperty(value = "close")
	private String close;
	@JsonProperty(value = "volume")
	private String volume;
	
	public String getDate() {
		return date;
	}
	public String getClose() {
		return close;
	}
	public String getVolume() {
		return volume;
	}
}