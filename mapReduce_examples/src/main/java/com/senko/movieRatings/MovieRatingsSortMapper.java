package com.senko.movieRatings;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class MovieRatingsSortMapper extends Mapper<Text, Text, IntWritable, Text> {
	
	private static int mapNumber = 0;
	IntWritable frequency = new IntWritable();
	
	@Override
	protected void setup(Context context) {
		System.out.println("sort mapper setup is wokring");
		this.mapNumber++;
		System.out.println("Sort-Map" + String.valueOf(this.mapNumber) + ": Start");
	}
	
	@Override
	public void map(Text key, Text value, Context context) {
		
		System.out.println("Movie Rating sort Mapper is working");
		
		int newVal = Integer.parseInt(value.toString());
		frequency.set(newVal);
		try {
			context.write(frequency, key);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void cleanup(Context context) {
		System.out.println("Sort-Map" + String.valueOf(this.mapNumber) + ": End");
	}

}
