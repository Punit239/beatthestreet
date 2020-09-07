package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityPublicInfoDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityPublicInfo;
import com.example.beatthestreet.model.iex.IexPublicInfo;
import com.example.beatthestreet.requests.EntityRequest;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class EntityPublicInfoService {

    @Autowired
    @Qualifier("publicInfoDao")
    EntityPublicInfoDao entityPublicInfoDao;

    private final LoadingCache<String, Optional<IexPublicInfo>> entityPublicInfoCache = CacheBuilder
            .newBuilder()
//            .maximumSize(Long.parseLong(env.getProperty("app.cache.news.maxsize")))
//            .expireAfterAccess(Long.parseLong(env.getProperty("app.cache.news.expire")), TimeUnit.SECONDS)
            .maximumSize(50)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Optional<IexPublicInfo> load(String entitySymbol) throws EntityDataNotFoundException {
                            Optional<IexPublicInfo> iexPublicInfo =
                                    entityPublicInfoDao.getEntityPublicInfo(entitySymbol);
                            return iexPublicInfo;
                        }
                    });

    public EntityPublicInfo getEntityPublicInfo(EntityRequest entityRequest) {

        EntityPublicInfo entityPublicInfo = null;
        Optional<IexPublicInfo> iexPublicInfo;
        try {
            iexPublicInfo = entityPublicInfoCache.get(entityRequest.getEntitySymbol());
            if(iexPublicInfo.isPresent()) {
                entityPublicInfo = convertResponseToEntityResponse(iexPublicInfo.get());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return entityPublicInfo;
    }

    private EntityPublicInfo convertResponseToEntityResponse(IexPublicInfo iexPublicInfo) {

        EntityPublicInfo entityPublicInfo = new EntityPublicInfo();
        entityPublicInfo.setSymbol(iexPublicInfo.getSymbol());
        entityPublicInfo.setCompanyName(iexPublicInfo.getCompanyName());
        entityPublicInfo.setWebsite(iexPublicInfo.getWebsite());
        entityPublicInfo.setDescription(iexPublicInfo.getDescription());
        entityPublicInfo.setCeo(iexPublicInfo.getCeo());
        entityPublicInfo.setEmployees(iexPublicInfo.getEmployees());
        return entityPublicInfo;
    }
}
