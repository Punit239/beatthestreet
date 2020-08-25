package com.example.beatthestreet.dao;

import com.example.beatthestreet.BeatTheStreetApplication;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.iex.IEXPriceHistory;
import com.example.beatthestreet.requests.EntityRequest;
import com.example.beatthestreet.utils.DeserializationUtil;
import com.example.beatthestreet.utils.HttpClientUtil;
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

    public Optional<IEXPriceHistory> getEntityPriceHistory(EntityRequest entityRequest) throws EntityDataNotFoundException {

        HashMap<String, String> iexEndPointMap = getIexEndPointMap(entityRequest.getEntitySymbol());
        List<NameValuePair> queryParams = getIexEndPointQueryParams();
        BeatTheStreetApplication.logger.info("Getting price history for " + entityRequest.getEntitySymbol() + " .");
        Optional<CloseableHttpResponse> iexPriceHistoryResponse = null;
        iexPriceHistoryResponse = HttpClientUtil.executeHttpGetRequest(iexEndPointMap, queryParams);
        IEXPriceHistory iexPriceHistory = null;
        if(iexPriceHistoryResponse.isPresent()) {
            int status = iexPriceHistoryResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexPriceHistory = (IEXPriceHistory) DeserializationUtil
                            .deserializeJsonString(EntityUtils.toString(iexPriceHistoryResponse.get().getEntity()), IEXPriceHistory.class);
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve price history for " + entityRequest.getEntitySymbol() +
                            ". HTTP response cannot be deserialized.");
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve price history for " + entityRequest.getEntitySymbol() +
                        ". Price history request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve price history for " + entityRequest.getEntitySymbol() +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexPriceHistory);
    }

    private List<NameValuePair> getIexEndPointQueryParams() {

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("types", env.getProperty("iex.queryparam.types.chart")));
        queryParams.add(new BasicNameValuePair("range", env.getProperty("iex.queryparam.range")));
        queryParams.add(new BasicNameValuePair("chartCloseOnly", env.getProperty("iex.queryparam.chartCloseOnly")));
        queryParams.add(new BasicNameValuePair("chartInterval", env.getProperty("iex.queryparam.chartInterval")));
        queryParams.add(new BasicNameValuePair("filter", env.getProperty("iex.queryparam.chart.filter")));
        queryParams.add(new BasicNameValuePair("token", env.getProperty("iex.api.key")));
        return queryParams;
    }

    private HashMap<String, String> getIexEndPointMap(String entitySymbol) {

        HashMap<String, String> iexEndPointMap = new HashMap<>();
        iexEndPointMap.put("scheme", "https");
        iexEndPointMap.put("host", env.getProperty("iex.host"));
        iexEndPointMap.put("path", env.getProperty("iex.resource") + "/" + entitySymbol + "/batch");
        return iexEndPointMap;
    }
}