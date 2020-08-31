package com.example.beatthestreet.model.entity;

public class EntityData {

    private EntityPriceHistory entityPriceHistory;
    private EntityFinancials entityFinancials;
    private EntityNews entityNews;

    public EntityPriceHistory getEntityPriceHistory() {
        return entityPriceHistory;
    }

    public void setEntityPriceHistory(EntityPriceHistory entityPriceHistory) {
        this.entityPriceHistory = entityPriceHistory;
    }

    public EntityNews getEntityNews() {
        return entityNews;
    }

    public void setEntityNews(EntityNews entityNews) {
        this.entityNews = entityNews;
    }

    public EntityFinancials getEntityFinancials() {
        return entityFinancials;
    }

    public void setEntityFinancials(EntityFinancials entityFinancials) {
        this.entityFinancials = entityFinancials;
    }

    public enum EntityDataTypes {

        PRICE_HISTORY,
        ANNUAL_FINANCIALS,
        QUARTERLY_FINANCIALS,
        NEWS
    }
}
