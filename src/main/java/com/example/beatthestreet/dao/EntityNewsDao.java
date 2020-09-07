package com.example.beatthestreet.dao;

import com.example.beatthestreet.BeatTheStreetApplication;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.iex.IexNewsRecord;
import com.example.beatthestreet.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository("newsDao")
@Configuration
@PropertySource({
        "classpath:application.properties",
        "classpath:appkeys.properties"
})
public class EntityNewsDao {

    @Autowired
    private Environment env;

public Optional<List<IexNewsRecord>> getEntityews(String entitySymbol) throws EntityDataNotFoundException {

        HashMap<String, String> iexEndPointMap = getIexEndPointMap(entitySymbol);
        List<NameValuePair> queryParams = getIexNewsEndPointQueryParams();
        BeatTheStreetApplication.logger.info("Getting news for " + entitySymbol + " .");
        Optional<CloseableHttpResponse> iexNewsResponse = HttpClientUtil.executeHttpGetRequest(iexEndPointMap, queryParams);
        List<IexNewsRecord> iexNewsRecords = null;
        if(iexNewsResponse.isPresent()) {
            int status = iexNewsResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexNewsRecords = new ObjectMapper().readValue(
                            EntityUtils.toString(iexNewsResponse.get().getEntity()), new TypeReference<List<IexNewsRecord>>() { });
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve news for " + entitySymbol +
                            ". HTTP response cannot be deserialized.");
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve news for " + entitySymbol +
                        ". News request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve news for " + entitySymbol +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexNewsRecords);
    }

    private List<NameValuePair> getIexNewsEndPointQueryParams() {

        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("token", env.getProperty("iex.api.key")));
        return queryParams;
    }

    private HashMap<String, String> getIexEndPointMap(String entitySymbol) {

        HashMap<String, String> iexEndPointMap = new HashMap<>();
        iexEndPointMap.put("scheme", "https");
        iexEndPointMap.put("host", env.getProperty("iex.host"));
        iexEndPointMap.put("path", env.getProperty("iex.resource") + "/" + entitySymbol + "/news/last/" + env.getProperty("iex.queryparam.news.last"));
        return iexEndPointMap;
    }
}
