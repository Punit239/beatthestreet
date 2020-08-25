package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityPriceDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityPriceHistory;
import com.example.beatthestreet.model.entity.EntityPriceHistoryRecord;
import com.example.beatthestreet.model.iex.IEXPriceHistory;
import com.example.beatthestreet.requests.EntityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EntityPriceService {

    @Autowired
    @Qualifier("priceDao")
    private EntityPriceDao entityPriceDao;

    @Async("asyncExecutor")
    @Cacheable(value = "entityPriceCache", key = "#entityRequest.entitySymbol")
    public CompletableFuture<EntityPriceHistory> getEntityHistoricalPrices(EntityRequest entityRequest) {

        Optional<IEXPriceHistory> iexPriceHistory = null;
        EntityPriceHistory entityPriceHistory = null;
        try {
            iexPriceHistory = entityPriceDao.getEntityPriceHistory(entityRequest);
            entityPriceHistory = convertDaoResponseToEntityResponse(iexPriceHistory.get());
        } catch (EntityDataNotFoundException e) {
            e.printStackTrace();
        }
        return entityPriceHistory == null ? null : CompletableFuture.completedFuture(entityPriceHistory);
    }

    private EntityPriceHistory convertDaoResponseToEntityResponse(IEXPriceHistory iexPriceHistory) {

        EntityPriceHistory entityPriceHistory = new EntityPriceHistory();
        List<EntityPriceHistoryRecord> entityPriceHistoryRecords = new ArrayList<>();
        iexPriceHistory.getIexPriceHistoryRecords().stream()
                .forEach(iexPriceHistoryRecord -> {
                    EntityPriceHistoryRecord entityPriceHistoryRecord = new EntityPriceHistoryRecord();
                    entityPriceHistoryRecord.setDate(iexPriceHistoryRecord.getDate());
                    entityPriceHistoryRecord.setClosingPrice(iexPriceHistoryRecord.getClose());
                    entityPriceHistoryRecords.add(entityPriceHistoryRecord);
                });
        entityPriceHistory.setEntityPriceHistoryRecords(entityPriceHistoryRecords);
        return entityPriceHistory;
    }
}
