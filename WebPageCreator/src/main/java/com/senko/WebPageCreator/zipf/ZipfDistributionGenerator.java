package com.senko.WebPageCreator.zipf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.util.Precision;

public class ZipfDistributionGenerator {
	
	List<Map<Integer, List<Integer>>> hyperLinkLinks;
	Random aRandom = new Random();
	
	public ZipfDistributionGenerator() {
		// init list
		hyperLinkLinks = new ArrayList<>();
		aRandom.setSeed(10);
	}
	
	// calculate zipfs distribution for each element
	public List<Map<Integer, List<Integer>>> computeLinks(int documentSize) {
		
		// step 1 -> compute zipf probability for each document
		// exponent we select 1
		ZipfDistribution zipf = new ZipfDistribution(documentSize, 1);
		
		// Map of nTh element and it's zipf probability
		Map<Integer, Double> zipfDists = new TreeMap<>();
		
		zipfDists = IntStream.range(1, documentSize + 1).boxed().collect(Collectors.toMap(e -> e, 
				e -> getZipfsProbability(zipf, e)));
		
		
		// step 2 -> create List of which pages will point which pages
		for(Map.Entry<Integer, Double> entry : zipfDists.entrySet()) {
			System.out.println("running for: " + entry.getKey() + " prob: " + entry.getValue());
			
			// for each element in map, calculate probability (how many links nTh page have)
			int sum = IntStream.range(1, documentSize + 1).map(index -> {
				
				int count = 0;
				Random newrandom = new Random();
				Double nextDouble = newrandom.nextDouble();
				
				if(nextDouble < entry.getValue()) {
					count++;
				}
				
				return count;
			}).sum();
			
			System.out.println("sum is : " + sum);
			
			// hangi sayfalar bu mevcut sayfaya link vericek onu bul
			
			IntStream stream = aRandom.ints(sum, 1, documentSize + 1).distinct();
			List<Integer> links = stream.boxed().collect(Collectors.toList());
			Map<Integer, List<Integer>> a = new HashMap<>();
			a.put(entry.getKey(), links);
			hyperLinkLinks.add(a);
			
		}
		
		return hyperLinkLinks;
	}
	
	private Double getZipfsProbability(ZipfDistribution zipf, int value) {
		
		return Precision.round(zipf.probability(value), 4);
		
	}
	
	

}
