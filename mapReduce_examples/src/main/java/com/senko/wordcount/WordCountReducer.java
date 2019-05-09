package com.senko.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	private static int reduceNumber = 0;
    @Override
    protected void setup(Context context) {
        this.reduceNumber++;
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": Start");
    }

    // reduce(C, D, Context)
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        String word = key.toString();
        int totalCount = 0;
        for (IntWritable value : values) {
            int count = value.get();
            totalCount += count;
        }
        context.write(new Text(word), new IntWritable(totalCount));
        // or you can use the following instead
        //context.write(key, new IntWritable(totalCount));
    }

    @Override
    protected void cleanup(Context context) {
        System.out.println("Reduce" + String.valueOf(this.reduceNumber) + ": End");
    }

}
