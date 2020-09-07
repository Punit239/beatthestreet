package com.example.beatthestreet.dao;

import com.example.beatthestreet.BeatTheStreetApplication;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.iex.IexPublicInfo;
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

@Repository("publicInfoDao")
public class EntityPublicInfoDao {

    @Autowired
    private Environment env;

    public Optional<IexPublicInfo> getEntityPublicInfo(String entitySymbol) throws EntityDataNotFoundException {

        HashMap<String, String> iexPublicInfoEndPointMap = getGenericInfoEndPointMap(entitySymbol);
        List<NameValuePair> queryParams = getGenericInfoQueryParams();
        BeatTheStreetApplication.logger.info("Getting public information for " + entitySymbol + ".");
        Optional<CloseableHttpResponse> iexPublicInfoResponse;
        iexPublicInfoResponse = HttpClientUtil.executeHttpGetRequest(iexPublicInfoEndPointMap, queryParams);
        IexPublicInfo iexPublicInfo;
        if(iexPublicInfoResponse.isPresent()) {
            int status = iexPublicInfoResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexPublicInfo = (IexPublicInfo) DeserializationUtil
                            .deserializeJsonString(EntityUtils.toString(iexPublicInfoResponse.get().getEntity()), IexPublicInfo.class);
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve public information for " + entitySymbol +
                            ". HTTP response cannot be deserialized.\n" + ioException);
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve public information for " + entitySymbol +
                        ". public information request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve public information for " + entitySymbol +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexPublicInfo);
    }

    private HashMap<String, String> getGenericInfoEndPointMap(String entitySymbol) {

        HashMap<String, String> iexLatestPriceInfoEndPointMap = new HashMap<>();
        iexLatestPriceInfoEndPointMap.put("scheme", "https");
        iexLatestPriceInfoEndPointMap.put("host", env.getProperty("iex.host"));
        iexLatestPriceInfoEndPointMap.put("path", env.getProperty("iex.resource") + "/" + entitySymbol + "/company");
        return iexLatestPriceInfoEndPointMap;
    }

    private List<NameValuePair> getGenericInfoQueryParams() {

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("filter", env.getProperty("iex.queryparam.company.filter")));
        queryParams.add(new BasicNameValuePair("token", env.getProperty("iex.api.key")));
        return queryParams;
    }
}
