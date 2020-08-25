package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityNewsDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityNews;
import com.example.beatthestreet.model.entity.EntityNewsRecord;
import com.example.beatthestreet.model.iex.IEXNews;
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
public class EntityNewsService {

    @Autowired
    @Qualifier("newsDao")
    private EntityNewsDao entityNewsDao;

    @Async("asyncExecutor")
    @Cacheable(value = "entityNewsCache", key = "#entityRequest.entitySymbol")
    public CompletableFuture<EntityNews> getEntityNews(EntityRequest entityRequest) {

        EntityNews entityNews = null;
        Optional<IEXNews> iexNews = null;
        try {
            iexNews = entityNewsDao.getEntityews(entityRequest);
            entityNews = convertResponseToEntityResponse(iexNews.get());
        } catch (EntityDataNotFoundException e) {
            e.printStackTrace();
        }
        return entityNews == null ? null : CompletableFuture.completedFuture(entityNews);
    }

    private EntityNews convertResponseToEntityResponse(IEXNews iexNews) {

        EntityNews entityNews = new EntityNews();
        List<EntityNewsRecord> entityNewsRecords = new ArrayList<>();
        iexNews.getIexNewsData().stream()
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
