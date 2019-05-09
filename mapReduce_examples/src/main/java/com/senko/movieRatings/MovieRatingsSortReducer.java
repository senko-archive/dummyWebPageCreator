package com.senko.movieRatings;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class MovieRatingsSortReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

	private static int reduceNumber = 0;
	Text word = new Text();

	@Override
	protected void setup(Context context) {
		this.reduceNumber++;
		System.out.println("Sort-Reduce" + String.valueOf(this.reduceNumber) + ": Start");
	}
	
	@Override
	public void reduce(IntWritable key, Iterable<Text> values, Context context) {
		
		System.out.println("movie rating sort reducer is working");
		
		for(Text value : values) {
			word.set(value);
			try {
				context.write(key, word);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
