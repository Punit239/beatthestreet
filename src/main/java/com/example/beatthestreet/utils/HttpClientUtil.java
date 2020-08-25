package com.example.demo.util;

import com.example.demo.DemoApplication;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpClientUtil {
	
	public static Optional<CloseableHttpResponse> executeHttpGetRequest(Map<String,String> path, List<NameValuePair> queryParams) {
		
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
				.build();
		CloseableHttpResponse response = null;
		URIBuilder uriBuilder = new URIBuilder();
		String url = null;
		uriBuilder
		.setScheme(path.get("scheme"))
		.setHost(path.get("host"))
		.setPath(path.get("path"))
		.setParameters(queryParams);
		try {
			url = uriBuilder.build().toURL().toString();
			DemoApplication.logger.info("URL reached : " + url);
			HttpGet getRequest = new HttpGet(uriBuilder.build());
			getRequest.setHeader("Content-Type", "application/json");
			response = httpClient.execute(getRequest);
//			response.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
//		try {
//			httpClient.close();
//		} catch (IOException ioException) {
//			ioException.printStackTrace();
//		}
		return Optional.ofNullable(response);
	}
}
