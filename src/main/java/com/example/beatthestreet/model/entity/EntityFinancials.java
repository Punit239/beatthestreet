package com.example.beatthestreet.model.entity;

import java.util.List;

public class EntityFinancials {

    private String symbol;
    private List<EntityFinancialsRecord> entityFinancialsRecords;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<EntityFinancialsRecord> getEntityFinancialsRecords() {
        return entityFinancialsRecords;
    }

    public void setEntityFinancialsRecords(List<EntityFinancialsRecord> entityFinancialsRecords) {
        this.entityFinancialsRecords = entityFinancialsRecords;
    }
}
