package com.senko.movieRatings;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class MovieRatingsMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	private static final IntWritable ONE = new IntWritable(1);
	private static int mapNumber = 0;
	
	@Override
	protected void setup(Context context) {
		this.mapNumber++;
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": Start");
	}
	
	@Override
	public void map(LongWritable longWritable, Text text, Context context) {
		String line = text.toString();
		String[] words = line.split("\t");
		try {
			context.write(new Text(words[1]), ONE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void cleanup(Context context) {
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": End");
	}

}
