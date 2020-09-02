package com.example.beatthestreet.service;

import com.example.beatthestreet.model.entity.EntityData;
import com.example.beatthestreet.model.entity.EntityFinancials;
import com.example.beatthestreet.model.entity.EntityNews;
import com.example.beatthestreet.model.entity.EntityPriceHistory;
import com.example.beatthestreet.requests.EntityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EntityDataService {

	@Autowired
	private EntityPriceService entityPriceService;
	@Autowired
	private EntityFinancialsService entityFinancialsService;
	@Autowired
	private EntityNewsService entityNewsService;
	private final ObjectMapper mapper = new ObjectMapper();
	 
	public EntityData getEntityData(EntityRequest entityRequest) throws Exception {

		EntityData entityData = new EntityData();
		CompletableFuture<EntityPriceHistory> entityPriceHistoryCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityPriceService.getEntityHistoricalPrices(entityRequest));
		CompletableFuture<EntityFinancials> entityFinancialsCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityFinancialsService.getEntityFinancials(entityRequest));
		CompletableFuture<EntityNews> entityNewsCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityNewsService.getEntityNews(entityRequest));
		entityData.setEntityPriceHistory(entityPriceHistoryCompletableFuture.get());
		entityData.setEntityFinancials(entityFinancialsCompletableFuture.get());
		entityData.setEntityNews(entityNewsCompletableFuture.get());
		return entityData;
	}
}