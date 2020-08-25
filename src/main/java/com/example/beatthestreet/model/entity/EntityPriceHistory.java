package com.example.beatthestreet.model.entity;

import java.util.List;

public class EntityPriceHistory {

    private List<EntityPriceHistoryRecord> entityPriceHistoryRecords;

    public List<EntityPriceHistoryRecord> getEntityPriceHistoryRecords() {
        return entityPriceHistoryRecords;
    }

    public void setEntityPriceHistoryRecords(List<EntityPriceHistoryRecord> entityPriceHistoryRecords) {
        this.entityPriceHistoryRecords = entityPriceHistoryRecords;
    }
}
