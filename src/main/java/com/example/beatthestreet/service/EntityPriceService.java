package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityPriceDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityPriceHistory;
import com.example.beatthestreet.model.entity.EntityPriceHistoryRecord;
import com.example.beatthestreet.model.iex.IEXPriceHistory;
import com.example.beatthestreet.requests.EntityRequest;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class EntityPriceService {

    @Autowired
    @Qualifier("priceDao")
    private EntityPriceDao entityPriceDao;
    @Autowired
    private Environment env;

    private final LoadingCache<String, Optional<IEXPriceHistory>> entityPriceHistoryCache = CacheBuilder
            .newBuilder()
//            .maximumSize(Long.parseLong(env.getProperty("app.cache.prices.maxsize")))
//            .maximumSize(50)
//            .expireAfterAccess(Long.parseLong(env.getProperty("app.cache.prices.expire")), TimeUnit.SECONDS)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Optional<IEXPriceHistory> load(String entitySymbol) throws EntityDataNotFoundException {
                            Optional<IEXPriceHistory> iexPriceHistory =
                                    entityPriceDao.getEntityPriceHistory(entitySymbol);
                            return iexPriceHistory;
                        }
                    });


    @Async("asyncExecutor")
    public CompletableFuture<EntityPriceHistory> getEntityHistoricalPrices(EntityRequest entityRequest) {

        Optional<IEXPriceHistory> iexPriceHistory = null;
        EntityPriceHistory entityPriceHistory = null;
        try {
            iexPriceHistory = entityPriceHistoryCache.get(entityRequest.getEntitySymbol());
            if(iexPriceHistory.isPresent()) {
                entityPriceHistory = convertDaoResponseToEntityResponse(iexPriceHistory.get());
            }
        } catch (ExecutionException e) {
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
