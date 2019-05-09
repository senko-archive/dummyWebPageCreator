package com.senko.sortDefault;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class DefaultSortMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
	
	private static int mapNumber = 0;
	
	@Override
	protected void setup(Context context) {
		this.mapNumber++;
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": Start");
	}

	public void map(LongWritable key, Text value, Context context) throws java.io.IOException, InterruptedException {
		String line = value.toString();
		System.out.println("line is: " + line);
		String[] tokens = line.split(","); // This is the delimiter between
		System.out.println(tokens[0]);
		System.out.println(tokens[1]);
		int keypart = Integer.parseInt(tokens[0]);
		int valuePart = Integer.parseInt(tokens[1]);
		context.write(new IntWritable(valuePart), new IntWritable(keypart));
	}
	
	@Override
	protected void cleanup(Context context) {
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": End");
	}
	
	

}
