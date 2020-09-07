package com.example.beatthestreet.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityLatestPriceInfo {

    private String open;
    private String high;
    private String low;
    private String latestPrice;
    private String marketCap;
    private String peRatio;
    private String week52High;
    private String week52Low;
    private String ytdChange;
}
