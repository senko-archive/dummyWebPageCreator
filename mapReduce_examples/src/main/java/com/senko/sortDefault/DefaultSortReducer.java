package com.senko.sortDefault;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class DefaultSortReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
	
	private static int reduceNumber = 0;
    @Override
    protected void setup(Context context) {
        this.reduceNumber++;
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": Start");
    }
	
	public void reduce(IntWritable key, Iterable<IntWritable> list, Context context) throws IOException, InterruptedException {
		for(IntWritable value : list) {
			context.write(value, key);
		}
	}
	
	@Override
    protected void cleanup(Context context) {
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": End");
    }

}
