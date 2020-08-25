package com.example.beatthestreet.dao;

import com.example.beatthestreet.BeatTheStreetApplication;
import com.example.beatthestreet.exceptions.EntityDataNotFoundException;
import com.example.beatthestreet.model.iex.IEXNews;
import com.example.beatthestreet.requests.EntityRequest;
import com.example.beatthestreet.utils.DeserializationUtil;
import com.example.beatthestreet.utils.HttpClientUtil;
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

    public Optional<IEXNews> getEntityews(EntityRequest entityRequest) throws EntityDataNotFoundException {

        HashMap<String, String> iexEndPointMap = getIexEndPointMap(entityRequest.getEntitySymbol());
        List<NameValuePair> queryParams = getIexNewsEndPointQueryParams();
        BeatTheStreetApplication.logger.info("Getting news for " + entityRequest.getEntitySymbol() + " .");
        Optional<CloseableHttpResponse> iexNewsResponse = HttpClientUtil.executeHttpGetRequest(iexEndPointMap, queryParams);
        IEXNews iexNews = null;
        if(iexNewsResponse.isPresent()) {
            int status = iexNewsResponse.get().getStatusLine().getStatusCode();
            if(status == 200) {
                try {
                    iexNews = (IEXNews) DeserializationUtil
                            .deserializeJsonString(EntityUtils.toString(iexNewsResponse.get().getEntity()), IEXNews.class);
                } catch (IOException ioException) {
                    throw new EntityDataNotFoundException("Unable to retrieve news for " + entityRequest.getEntitySymbol() +
                            ". HTTP response cannot be deserialized.");
                }
            } else {
                throw new EntityDataNotFoundException("Unable to retrieve news for " + entityRequest.getEntitySymbol() +
                        ". News request returned with status : " + status);
            }
        } else {
            throw new EntityDataNotFoundException("Unable to retrieve news for " + entityRequest.getEntitySymbol() +
                    ". Incorrect HTTP request structure.");
        }
        return Optional.ofNullable(iexNews);
    }

    private List<NameValuePair> getIexNewsEndPointQueryParams() {

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("types", env.getProperty("iex.queryparam.types.news")));
        queryParams.add(new BasicNameValuePair("last", env.getProperty("iex.queryparam.last")));
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
