package com.senko.movieRatings;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class MovieRatingsReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	private static int reduceNumber = 0;
    @Override
    protected void setup(Context context) {
        this.reduceNumber++;
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": Start");
    }
    
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context){
    	//System.out.println("movie rating reducer is working");
        String word = key.toString();
        int totalCount = 0;
        for (IntWritable value : values) {
            int count = value.get();
            totalCount += count;
            //System.out.println("totalCount: " + totalCount);
        }
        try {
			context.write(new Text(word), new IntWritable(totalCount));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // or you can use the following instead
        //context.write(key, new IntWritable(totalCount));
    }
    
    @Override
    protected void cleanup(Context context) {
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": End");
    }

}
