package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IEXFinancials {

	@JsonProperty("symbol")
	private String symbol;
	@JsonProperty("financials")
	private List<IEXFinancialsRecord> iexFinancialsRecords;

    public String getSymbol() {
        return symbol;
    }

    public List<IEXFinancialsRecord> getIexFinancialsRecords() {
        return iexFinancialsRecords;
    }
}