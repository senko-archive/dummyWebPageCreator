package com.senko.WebPageCreator.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ConsumerHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsumerHelper.class);
	
	public static ResponseEntity<String> getResponse(RestTemplate restTemplate, Map<String, String> headerOptions, String url) {
		logger.debug("ConsumerHelper.getResponse() now working"); 
		
		HttpHeaders headers = new HttpHeaders();
		headerOptions.forEach((k, v) -> headers.add(k, v));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		return response;
	}
	
	public static ResponseEntity<String> postResponse(RestTemplate restTemplate, Map<String, String> headerOptions, String url) {
		logger.debug("ConsumerHelper.postResponse() now working");
		
		HttpHeaders headers = new HttpHeaders();
		headerOptions.forEach((k, v) -> headers.add(k, v));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		if(response == null) {
			logger.error("http post response is null");
		}
		else {
			System.out.println("http post response is not null no problem");
		}
		
		return response;
	}

}
