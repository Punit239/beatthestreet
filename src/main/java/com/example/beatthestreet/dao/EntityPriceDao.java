package com.example.beatthestreet.dao;

import com.example.beatthestreet.BeatTheStreetApplication;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.iex.IexLatestPriceInfo;
import com.example.beatthestreet.model.iex.IexPriceHistoryRecord;
import com.example.beatthestreet.utils.DeserializationUtil;
import com.example.beatthestreet.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository("priceDao")
public class EntityPriceDao {

    @Autowired
    private Environment env;

    public Optional<List<IexPriceHistoryRecord>> getEntityPriceHistory(String entitySymbol) throws EntityDataNotFoundException {

        HashMap<String, String> iexPriceHistoryEndPointMap = getPriceHistoryEndPointMap(entitySymbol);
        List<NameValuePair> queryParams = getPriceHistoryQueryParams();
        BeatTheStreetApplication.logger.info("Getting price history for " + entitySymbol + " .");
        Optional<CloseableHttpResponse> iexPriceHistoryResponse;
        iexPriceHistoryResponse = HttpClientUtil.executeHttpGetRequest(iexPriceHistoryEndPointMap, queryParams);
        List<IexPriceHistoryRecord> iexPriceHistoryRecords;
        if(iexPriceHistoryResponse.isPresent()) {
            int status = iexPriceHistoryResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexPriceHistoryRecords = new ObjectMapper().readValue(
                            EntityUtils.toString(iexPriceHistoryResponse.get().getEntity()), new TypeReference<List<IexPriceHistoryRecord>>() { });
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve price history for " + entitySymbol +
                            ". HTTP response cannot be deserialized.\n" + ioException);
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve price history for " + entitySymbol +
                        ". Price history request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve price history for " + entitySymbol +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexPriceHistoryRecords);
    }

    public Optional<IexLatestPriceInfo> getLatestPriceInfo(String entitySymbol) throws EntityDataNotFoundException {

        HashMap<String, String> iexEndPointMap = getLatestPriceEndPointMap(entitySymbol);
        List<NameValuePair> queryParams = getLatestPriceQueryParams();
        BeatTheStreetApplication.logger.info("Getting latest price information for " + entitySymbol + ".");
        Optional<CloseableHttpResponse> iexLatestPriceInfoResponse;
        iexLatestPriceInfoResponse = HttpClientUtil.executeHttpGetRequest(iexEndPointMap, queryParams);
        IexLatestPriceInfo iexLatestPriceInfo;
        if(iexLatestPriceInfoResponse.isPresent()) {
            int status = iexLatestPriceInfoResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexLatestPriceInfo = (IexLatestPriceInfo) DeserializationUtil
                            .deserializeJsonString(EntityUtils.toString(iexLatestPriceInfoResponse.get().getEntity()), IexLatestPriceInfo.class);
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve latest price information for " + entitySymbol +
                            ". HTTP response cannot be deserialized.\n" + ioException);
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve latest price information for " + entitySymbol +
                        ". Latest price informatioL request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve latest price information for " + entitySymbol +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexLatestPriceInfo);
    }

    private HashMap<String, String> getLatestPriceEndPointMap(String entitySymbol) {

        HashMap<String, String> iexLatestPriceInfoEndPointMap = new HashMap<>();
        iexLatestPriceInfoEndPointMap.put("scheme", "https");
        iexLatestPriceInfoEndPointMap.put("host", env.getProperty("iex.host"));
        iexLatestPriceInfoEndPointMap.put("path", env.getProperty("iex.resource") + "/" + entitySymbol + "/quote");
        return iexLatestPriceInfoEndPointMap;
    }

    private List<NameValuePair> getLatestPriceQueryParams() {

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("filter", env.getProperty("iex.queryparam.quote.filter")));
        queryParams.add(new BasicNameValuePair("token", env.getProperty("iex.api.key")));
        return queryParams;
    }

    private HashMap<String, String> getPriceHistoryEndPointMap(String entitySymbol) {

        HashMap<String, String> iexPriceHistoryEndPointMap = new HashMap<>();
        iexPriceHistoryEndPointMap.put("scheme", "https");
        iexPriceHistoryEndPointMap.put("host", env.getProperty("iex.host"));
        iexPriceHistoryEndPointMap.put("path", env.getProperty("iex.resource") + "/" + entitySymbol + "/chart");
        return iexPriceHistoryEndPointMap;
    }

    private List<NameValuePair> getPriceHistoryQueryParams() {

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("range", env.getProperty("iex.queryparam.range")));
        queryParams.add(new BasicNameValuePair("chartCloseOnly", env.getProperty("iex.queryparam.chartCloseOnly")));
        queryParams.add(new BasicNameValuePair("chartInterval", env.getProperty("iex.queryparam.chartInterval")));
        queryParams.add(new BasicNameValuePair("filter", env.getProperty("iex.queryparam.chart.filter")));
        queryParams.add(new BasicNameValuePair("token", env.getProperty("iex.api.key")));
        return queryParams;
    }
}