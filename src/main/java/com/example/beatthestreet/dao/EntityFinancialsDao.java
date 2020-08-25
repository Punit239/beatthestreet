package com.example.beatthestreet.dao;

import com.example.beatthestreet.BeatTheStreetApplication;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.iex.IEXFinancials;
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

@Repository("financesDao")
public class EntityFinancialsDao {

    @Autowired
    private Environment env;

    public Optional<IEXFinancials> getFinancialData(EntityRequest entityRequest) throws EntityDataNotFoundException {

        HashMap<String, String> iexFinancialsEndPointMap = getIexFinancialsEndPointMap(entityRequest.getEntitySymbol());
        List<NameValuePair> iexFinancialsQueryParams = getIexFinancialsQueryParams(entityRequest.getDataType());
        BeatTheStreetApplication.logger.info("Getting financials for " + entityRequest.getEntitySymbol() + " .");
        Optional<CloseableHttpResponse> iexFinancialResponse = HttpClientUtil.executeHttpGetRequest(iexFinancialsEndPointMap, iexFinancialsQueryParams);
        IEXFinancials iexFinancials = null;
        if(iexFinancialResponse.isPresent()) {
            int status = iexFinancialResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexFinancials = (IEXFinancials) DeserializationUtil
                            .deserializeJsonString(EntityUtils.toString(iexFinancialResponse.get().getEntity()), IEXFinancials.class);
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve financials for " + entityRequest.getEntitySymbol() +
                            ". HTTP response cannot be deserialized.");
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve financials for " + entityRequest.getEntitySymbol() +
                        ". Financials request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve financials for " + entityRequest.getEntitySymbol() +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexFinancials);
    }

    private List<NameValuePair> getIexFinancialsQueryParams(String dataType) {
        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        if(dataType.equals("annual")) {
            queryParams.add(new BasicNameValuePair("period", env.getProperty("iex.queryparam.period.annual")));
        } else if(dataType.equals("quarter")) {
            queryParams.add(new BasicNameValuePair("period", env.getProperty("iex.queryparam.period.quarter")));
        }
        queryParams.add(new BasicNameValuePair("token", env.getProperty("iex.api.key")));
        queryParams.add(new BasicNameValuePair("last", env.getProperty("iex.queryparam.last")));
        return queryParams;
    }

    private HashMap<String, String> getIexFinancialsEndPointMap(String entitySymbol) {

        HashMap<String, String> iexEndPointMap = new HashMap<>();
        iexEndPointMap.put("scheme", "https");
        iexEndPointMap.put("host", env.getProperty("iex.host"));
        iexEndPointMap.put("path", env.getProperty("iex.resource") + "/" + entitySymbol + "/financials");
        return iexEndPointMap;
    }
}