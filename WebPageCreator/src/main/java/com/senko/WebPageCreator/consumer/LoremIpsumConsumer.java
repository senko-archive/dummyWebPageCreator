package com.senko.WebPageCreator.consumer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.senko.WebPageCreator.utils.ConsumerHelper;

@Component
public class LoremIpsumConsumer {
	
	@Autowired
	RestTemplate restTemplate;
	
	public StringBuilder getLoremIpsum(int paragraphSize) {
		
		// prepare rest request
		StringBuilder request = new StringBuilder();
		request.append("https://loripsum.net/api").append("/").append(paragraphSize).append("/long");
		
		Map<String, String> requestHeader = new HashMap<>();
		requestHeader.put("Accept", "application/json");
		System.out.println(request.toString());
		ResponseEntity<String> response = ConsumerHelper.getResponse(restTemplate, requestHeader, request.toString());
		
		StringBuilder responseBuffer = new StringBuilder(response.getBody().toString());
		
		return responseBuffer;
	}

}
