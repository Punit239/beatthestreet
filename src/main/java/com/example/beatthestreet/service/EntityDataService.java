package com.example.beatthestreet.service;

import com.example.beatthestreet.model.entity.*;
import com.example.beatthestreet.requests.EntityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EntityDataService {

	@Autowired
	private EntityPriceService entityPriceService;
	@Autowired
	private EntityFinancialsService entityFinancialsService;
	@Autowired
	private EntityNewsService entityNewsService;
	@Autowired
	private EntityPublicInfoService entityPublicInfoService;
	 
	public EntityData getEntityData(EntityRequest entityRequest) throws Exception {

		EntityData entityData = new EntityData();
		CompletableFuture<EntityLatestPriceInfo> entityLatestPriceInfoFuture =
				CompletableFuture.supplyAsync(() -> entityPriceService.getEntityLatestPriceInfo(entityRequest));
		CompletableFuture<EntityPriceHistory> entityPriceHistoryCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityPriceService.getEntityHistoricalPrices(entityRequest));
		CompletableFuture<EntityNews> entityNewsCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityNewsService.getEntityNews(entityRequest));
		entityData.setEntityLatestPriceInfo(entityLatestPriceInfoFuture.get());
		entityData.setEntityPriceHistory(entityPriceHistoryCompletableFuture.get());
		entityData.setEntityNews(entityNewsCompletableFuture.get());
		return entityData;
	}

	public EntityFinancials getEntityFinancials(EntityRequest entityRequest) throws ExecutionException, InterruptedException {

		CompletableFuture<EntityFinancials> entityFinancialsCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityFinancialsService.getEntityFinancials(entityRequest));
		return entityFinancialsCompletableFuture.get();
	}

	public EntityPublicInfo getEntityPublicInfo(EntityRequest entityRequest) throws ExecutionException, InterruptedException {

		CompletableFuture<EntityPublicInfo> entityPublicInfoCompletableFuture =
				CompletableFuture.supplyAsync(() -> entityPublicInfoService.getEntityPublicInfo(entityRequest));
		return entityPublicInfoCompletableFuture.get();
	}
}