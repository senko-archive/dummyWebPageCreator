package com.senko.WebPageCreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.senko.WebPageCreator.consumer.LoremIpsumConsumer;
import com.senko.WebPageCreator.zipf.ZipfDistributionGenerator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Runner {
	
	String folderName = "websites";
	int webSiteSize = 100;
	
	Map<Integer, List<Map<Integer, Integer>>> referencesMap;
	
	@Autowired
	LoremIpsumConsumer loremIpsumConsumer;
	
	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	public Runner() {
		
	}
	
	public void start() {
		
		// create main folder
		createFolder(folderName);
		
		// create link connection
		List<Map<Integer, List<Integer>>> zipfList = getZipfDistribution(webSiteSize);
				
		// find which site will create hyperlink to which one
		referencesMap = calculateReferences(zipfList);
		
		// create web site than get lorem ipsum then put into website than create link
		createWebSites(webSiteSize);
		
		
		
		
				
	}
	
	private Boolean createFolder(String folderName) {
		
		File f = new File(folderName);
		
		try {
			FileUtils.cleanDirectory(f);
			FileUtils.forceDelete(f);
			FileUtils.forceMkdir(f);
			return true;
		} catch (IOException e) {
			log.error(e.getMessage());
			return false;
		}
	}
	
	private Boolean createWebSites(int webSiteNumber) {
		
		// generate names for files
		String fileBaseName = "document_";
		IntStream.range(1, webSiteNumber + 1).boxed().map(e -> {
			String fileName = fileBaseName+e.toString()+".html";
			return fileName;
		}).forEach(fileName -> createWebSite(fileName));
		
		return true;
	}
	
	private Boolean createWebSite(String fileName) {
		
		try {
			File webSite = new File(folderName+"/"+fileName);
			Random rand = new Random();
			int size = rand.nextInt(10);
			StringBuilder header = new StringBuilder("<header><h1>").append(fileName).append("</h1></header>");
			StringBuilder content = loremIpsumConsumer.getLoremIpsum(size);
			header.append(content);
			
			// buraya hangi link yaraticagini soyle.
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(fileName);
			int pageNumber = 0;
			while(m.find()) {
				pageNumber = Integer.parseInt(m.group());
			}
			
			List<Map<Integer, Integer>> valueList = referencesMap.get(pageNumber);
			if(!valueList.isEmpty()) {
				for(Map<Integer, Integer> entrySet : valueList) {
					int referenceTo = (int) entrySet.keySet().toArray()[0];
					int repeatNumber = (int) entrySet.values().toArray()[0];
					StringBuilder aHrefLink = createHtmlLink(referenceTo, repeatNumber);
					header.append(aHrefLink);
				}
			}
			else {
				log.debug("for key: " + pageNumber + "list is empty");
			}
			
			
			
			FileUtils.writeStringToFile(webSite, header.toString());
			
			return true;
			
		} catch (IOException e) {
			log.error(e.getMessage());
			return false;
		}
		
	}
	
	private StringBuilder createHtmlLink(int referenceTo, int repeatNumber) {
		
		StringBuilder aHrefLink = new StringBuilder();
		aHrefLink.append("<a href=").append("\"").append(".").append("/").append("document_").append(referenceTo).append(".html").append("\"").append(">").append("asd").append("</a>");
		System.out.println("link in: " + referenceTo + " -> " +  aHrefLink.toString());
		return aHrefLink;
	}

	private List<Map<Integer, List<Integer>>> getZipfDistribution(int size) {
		ZipfDistributionGenerator generator = new ZipfDistributionGenerator();
		return generator.computeLinks(size);
	}
	
	private Map<Integer, List<Map<Integer, Integer>>> calculateReferences(List<Map<Integer, List<Integer>>> zipfList) {
		
		List<Integer> webSiteNumberList = IntStream.range(1, webSiteSize + 1).boxed().collect(Collectors.toList());
		Map<Integer, List<Map<Integer, Integer>>> referenceMap = new HashMap<>();
		
		for(Integer webSiteNumber : webSiteNumberList) {
			List<Map<Integer, Integer>> aList = new ArrayList<>();
			for(Map<Integer, List<Integer>> entry : zipfList) {
				
				if((Integer) entry.keySet().toArray()[0] == webSiteNumber) {
					// bir sonraki for dongusune gec, kendi kendine referans veren olmamamli
					continue;
				}
				else {	
					
					// convert collection to a single list than look for reference sites
					int size = entry.values().stream().flatMap(x -> x.stream().map(y -> y)).collect(Collectors.toList())
						.stream().filter(number -> number == webSiteNumber).collect(Collectors.toList()).size();
					
					//int size = entry.values().stream().flatMap(item -> item.stream().filter(x -> x == webSiteNumber).map(x -> x)).collect(Collectors.toList()).size();
					if(size > 0) {
						Map<Integer, Integer> referenced = new HashMap<>();
						referenced.put((Integer) entry.keySet().toArray()[0], size);
						aList.add(referenced);
					}
					
				}
					
			}
			
			referenceMap.put(webSiteNumber, aList);
		}
		
		
		// print on console
		Iterator<Entry<Integer, List<Map<Integer, Integer>>>> iterator = referenceMap.entrySet().iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		
		return referenceMap; 
		
	}

}
