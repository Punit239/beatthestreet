package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IexPriceHistoryRecord {

	@JsonProperty(value = "date")
	private String date;
	@JsonProperty(value = "close")
	private String close;
	@JsonProperty(value = "volume")
	private String volume;
}