package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IexLatestPriceInfo {

    @JsonProperty("open")
    private String open;
    @JsonProperty("high")
    private String high;
    @JsonProperty("low")
    private String low;
    @JsonProperty("latestPrice")
    private String latestPrice;
    @JsonProperty("marketCap")
    private String marketCap;
    @JsonProperty("peRatio")
    private String peRatio;
    @JsonProperty("week52High")
    private String week52High;
    @JsonProperty("week52Low")
    private String week52Low;
    @JsonProperty("ytdChange")
    private String ytdChange;
}
