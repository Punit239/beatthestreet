package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityNewsDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityNews;
import com.example.beatthestreet.model.entity.EntityNewsRecord;
import com.example.beatthestreet.model.iex.IexNewsRecord;
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
public class EntityNewsService {

    @Autowired
    @Qualifier("newsDao")
    private EntityNewsDao entityNewsDao;
    @Autowired
    private Environment env;

    private final LoadingCache<String, Optional<List<IexNewsRecord>>> entityNewsCache = CacheBuilder
            .newBuilder()
//            .maximumSize(Long.parseLong(env.getProperty("app.cache.news.maxsize")))
//            .expireAfterAccess(Long.parseLong(env.getProperty("app.cache.news.expire")), TimeUnit.SECONDS)
            .maximumSize(50)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Optional<List<IexNewsRecord>> load(String entitySymbol) throws EntityDataNotFoundException {
                            Optional<List<IexNewsRecord>> iexNewsRecords =
                                    entityNewsDao.getEntityews(entitySymbol);
                            return iexNewsRecords;
                        }
                    });

    public EntityNews getEntityNews(EntityRequest entityRequest) {

        EntityNews entityNews = null;
        Optional<List<IexNewsRecord>> iexNewsRecords = null;
        try {
            iexNewsRecords = entityNewsCache.get(entityRequest.getEntitySymbol());
            if(iexNewsRecords.isPresent()) {
                entityNews = convertResponseToEntityResponse(iexNewsRecords.get());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return entityNews;
    }

    private EntityNews convertResponseToEntityResponse(List<IexNewsRecord> iexNewsRecords) {

        EntityNews entityNews = new EntityNews();
        List<EntityNewsRecord> entityNewsRecords = new ArrayList<>();
        iexNewsRecords.stream()
                .forEach(iexNewsRecord -> {
                    EntityNewsRecord entityNewsRecord = new EntityNewsRecord();
                    entityNewsRecord.setHeadline(iexNewsRecord.getHeadline());
                    entityNewsRecord.setUrl(iexNewsRecord.getUrl());
                    entityNewsRecord.setSource(iexNewsRecord.getSource());
                    entityNewsRecord.setDate(iexNewsRecord.getDateTime());
                    entityNewsRecords.add(entityNewsRecord);
                });
        entityNews.setEntityNewsRecords(entityNewsRecords);
        return entityNews;
    }
}
