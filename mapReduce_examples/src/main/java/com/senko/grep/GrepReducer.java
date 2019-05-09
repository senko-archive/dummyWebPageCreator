package com.senko.grep;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class GrepReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
	
	private LongWritable result = new LongWritable();
	private static int reduceNumber = 0;
	
    @Override
    protected void setup(Context context) {
        this.reduceNumber++;
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": Start");
    }
	
	public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
		
		System.out.println("inside the reducer");
		long sum = 0;
		for (LongWritable val : values) {
		      sum += val.get();
		    }
		result.set(sum);
	    context.write(key, result);

	}
	
	 @Override
	    protected void cleanup(Context context) {
	        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": End");
	    }

}
