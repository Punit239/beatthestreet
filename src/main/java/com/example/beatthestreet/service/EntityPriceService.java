package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityPriceDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityLatestPriceInfo;
import com.example.beatthestreet.model.entity.EntityPriceHistory;
import com.example.beatthestreet.model.entity.EntityPriceHistoryRecord;
import com.example.beatthestreet.model.iex.IexLatestPriceInfo;
import com.example.beatthestreet.model.iex.IexPriceHistoryRecord;
import com.example.beatthestreet.requests.EntityRequest;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class EntityPriceService {

    @Autowired
    @Qualifier("priceDao")
    private EntityPriceDao entityPriceDao;
    @Autowired
    private Environment env;

    private final LoadingCache<String, Optional<List<IexPriceHistoryRecord>>> entityPriceHistoryCache = CacheBuilder
            .newBuilder()
//            .maximumSize(Long.parseLong(env.getProperty("app.cache.prices.maxsize")))
//            .expireAfterAccess(Long.parseLong(env.getProperty("app.cache.prices.expire")), TimeUnit.SECONDS)
            .maximumSize(50)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Optional<List<IexPriceHistoryRecord>> load(String entitySymbol) throws EntityDataNotFoundException {
                            Optional<List<IexPriceHistoryRecord>> iexPriceHistoryRecords =
                                    entityPriceDao.getEntityPriceHistory(entitySymbol);
                            return iexPriceHistoryRecords;
                        }
                    });

    private final LoadingCache<String, Optional<IexLatestPriceInfo>> entityLatestPriceInfoCache = CacheBuilder
            .newBuilder()
//            .maximumSize(Long.parseLong(env.getProperty("app.cache.prices.maxsize")))
//            .expireAfterAccess(Long.parseLong(env.getProperty("app.cache.prices.expire")), TimeUnit.SECONDS)
            .maximumSize(50)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Optional<IexLatestPriceInfo> load(String entitySymbol) throws EntityDataNotFoundException {
                            Optional<IexLatestPriceInfo> iexLatestPriceInfo =
                                    entityPriceDao.getLatestPriceInfo(entitySymbol);
                            return iexLatestPriceInfo;
                        }
                    });

    public EntityPriceHistory getEntityHistoricalPrices(EntityRequest entityRequest) {

        Optional<List<IexPriceHistoryRecord>> iexPriceHistoryRecords = null;
        EntityPriceHistory entityPriceHistory = null;
        try {
            iexPriceHistoryRecords = entityPriceHistoryCache.get(entityRequest.getEntitySymbol());
            if(iexPriceHistoryRecords.isPresent()) {
                entityPriceHistory = convertDaoResponseToEntityResponse(iexPriceHistoryRecords.get());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return entityPriceHistory;
    }

    public EntityLatestPriceInfo getEntityLatestPriceInfo(EntityRequest entityRequest) {

        Optional<IexLatestPriceInfo> iexLatestPriceInfo = null;
        EntityLatestPriceInfo entityLatestPriceInfo = null;
        try {
            iexLatestPriceInfo = entityLatestPriceInfoCache.get(entityRequest.getEntitySymbol());
            if(iexLatestPriceInfo.isPresent()) {
                entityLatestPriceInfo = convertDaoLatestInfoToEntityLatestInfo(iexLatestPriceInfo.get());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return entityLatestPriceInfo;
    }

    private EntityPriceHistory convertDaoResponseToEntityResponse(List<IexPriceHistoryRecord> iexPriceHistoryRecords) {

        EntityPriceHistory entityPriceHistory = new EntityPriceHistory();
        List<EntityPriceHistoryRecord> entityPriceHistoryRecords = new ArrayList<>();
        iexPriceHistoryRecords.stream()
                .forEach(iexPriceHistoryRecord -> {
                    EntityPriceHistoryRecord entityPriceHistoryRecord = new EntityPriceHistoryRecord();
                    entityPriceHistoryRecord.setDate(iexPriceHistoryRecord.getDate());
                    entityPriceHistoryRecord.setClosingPrice(iexPriceHistoryRecord.getClose());
                    entityPriceHistoryRecords.add(entityPriceHistoryRecord);
                });
        entityPriceHistory.setEntityPriceHistoryRecords(entityPriceHistoryRecords);
        return entityPriceHistory;
    }

    private EntityLatestPriceInfo convertDaoLatestInfoToEntityLatestInfo(IexLatestPriceInfo iexLatestPriceInfo) {

        EntityLatestPriceInfo entityLatestPriceInfo = new EntityLatestPriceInfo();
        entityLatestPriceInfo.setOpen(iexLatestPriceInfo.getOpen());
        entityLatestPriceInfo.setHigh(iexLatestPriceInfo.getHigh());
        entityLatestPriceInfo.setLow(iexLatestPriceInfo.getLow());
        entityLatestPriceInfo.setLatestPrice(iexLatestPriceInfo.getLatestPrice());
        entityLatestPriceInfo.setMarketCap(iexLatestPriceInfo.getMarketCap());
        entityLatestPriceInfo.setPeRatio(iexLatestPriceInfo.getPeRatio());
        entityLatestPriceInfo.setWeek52High(iexLatestPriceInfo.getWeek52High());
        entityLatestPriceInfo.setWeek52Low(iexLatestPriceInfo.getWeek52Low());
        entityLatestPriceInfo.setYtdChange(iexLatestPriceInfo.getYtdChange());
        return entityLatestPriceInfo;
    }
}
