package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IexFinancials {

	@JsonProperty("symbol")
	private String symbol;
	@JsonProperty("financials")
	private List<IexFinancialsRecord> iexFinancialsRecords;
}