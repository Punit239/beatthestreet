package com.example.beatthestreet.model.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityData {

    private EntityLatestPriceInfo entityLatestPriceInfo;
    private EntityPriceHistory entityPriceHistory;
    private EntityNews entityNews;
}
