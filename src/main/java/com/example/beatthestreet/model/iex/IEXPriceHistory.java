package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class IEXPriceHistory {

    @JsonProperty("chart")
    private List<IEXPriceHistoryRecord> iexPriceHistoryRecords;
}