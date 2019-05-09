package com.senko.grep;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class GrepMapper<K> extends Mapper<K, Text, Text, LongWritable> {
	
	public static String PATTERN = "mapreduce.mapper.regex";
	private Pattern pattern;
	private static int mapNumber = 0;
	
	public void setup(Context context) {
		this.mapNumber++;
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": Start");
		Configuration conf = context.getConfiguration();
		pattern = Pattern.compile(conf.get(PATTERN));
		
	}
	
	public void map(K key, Text value, Context context) throws IOException, InterruptedException {
		
		System.out.println("inside the mapper");
		String text = value.toString();
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()) {
			System.out.println(matcher.group(0));
			context.write(new Text(matcher.group(0)), new LongWritable(1));
		}
		
		System.out.println("mapper is out");
		
	}
	
	@Override
	protected void cleanup(Context context) {
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": End");
	}

}
