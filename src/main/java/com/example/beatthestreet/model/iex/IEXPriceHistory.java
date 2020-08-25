package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IEXPriceHistory {

    @JsonProperty("chart")
    private List<IEXPriceHistoryRecord> iexPriceHistoryRecords;

    public List<IEXPriceHistoryRecord> getIexPriceHistoryRecords() {
        return iexPriceHistoryRecords;
    }
}