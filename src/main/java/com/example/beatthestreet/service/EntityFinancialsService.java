package com.example.beatthestreet.service;

import com.example.beatthestreet.dao.EntityFinancialsDao;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.entity.EntityFinancials;
import com.example.beatthestreet.model.entity.EntityFinancialsRecord;
import com.example.beatthestreet.model.iex.IexFinancials;
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
public class EntityFinancialsService {

    @Autowired
    @Qualifier("financesDao")
    private EntityFinancialsDao entityFinancialsDao;
    @Autowired
    private Environment env;

    private final LoadingCache<EntityRequest, Optional<IexFinancials>> entityFinancialsCache = CacheBuilder
            .newBuilder()
//            .maximumSize(Long.parseLong(env.getProperty("app.cache.financials.maxsize")))
//            .expireAfterAccess(Long.parseLong(env.getProperty("app.cache.financials.expire")), TimeUnit.SECONDS)
            .maximumSize(50)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<>() {
                        @Override
                        public Optional<IexFinancials> load(EntityRequest entityRequest) throws EntityDataNotFoundException {
                            Optional<IexFinancials> iexFinancials =
                                    entityFinancialsDao.getFinancialData(entityRequest);
                            return iexFinancials;
                        }
                    });

    public EntityFinancials getEntityFinancials(EntityRequest entityRequest) {

        EntityFinancials entityFinancials = null;
        Optional<IexFinancials> iexFinancials = null;
        try {
            iexFinancials = entityFinancialsCache.get(entityRequest);
            if(iexFinancials.isPresent()) {
                entityFinancials = convertDaoResponseToEntityResponse(iexFinancials.get(), entityRequest);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return entityFinancials;
    }

    private EntityFinancials convertDaoResponseToEntityResponse(IexFinancials iexFinancials, EntityRequest entityRequest) {

        EntityFinancials entityFinancials = new EntityFinancials();
        List<EntityFinancialsRecord> entityFinancialsRecords = new ArrayList<>();
        iexFinancials.getIexFinancialsRecords().stream()
                .forEach(iexFinancialsRecord -> {
                            EntityFinancialsRecord entityFinancialsRecord = new EntityFinancialsRecord();
                            entityFinancialsRecord.setFiscalYear(iexFinancialsRecord.getFiscalDate());
                            String formType = null;
                            if(entityRequest.getPeriod().equals("annual")) {
                                formType = "10K";
                            } else if (entityRequest.getPeriod().equals("quarter")) {
                                formType = "10Q";
                            }
                            entityFinancialsRecord.setFiscalQuarter("4");
                            entityFinancialsRecord.setFormType(formType);
                            entityFinancialsRecord.setTotalRevenue(iexFinancialsRecord.getTotalRevenue());
                            entityFinancialsRecord.setGrossProfit(iexFinancialsRecord.getGrossProfit());
                            entityFinancialsRecord.setCurrency(iexFinancialsRecord.getCurrency());
                            entityFinancialsRecord.setNetIncome(iexFinancialsRecord.getNetIncome());
                            entityFinancialsRecord.setCashFlow(iexFinancialsRecord.getCashFlow());
                            entityFinancialsRecord.setTotalLongTermDebt(iexFinancialsRecord.getTotalLongTermDebt());
                            entityFinancialsRecords.add(entityFinancialsRecord);
                        });
        entityFinancials.setEntityFinancialsRecords(entityFinancialsRecords);
        return entityFinancials;
    }
}
