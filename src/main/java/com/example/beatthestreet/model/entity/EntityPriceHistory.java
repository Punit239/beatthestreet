package com.example.beatthestreet.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EntityPriceHistory {

    private List<EntityPriceHistoryRecord> entityPriceHistoryRecords;
}
