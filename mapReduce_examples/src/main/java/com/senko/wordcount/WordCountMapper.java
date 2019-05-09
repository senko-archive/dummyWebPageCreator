package com.senko.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private static final IntWritable ONE = new IntWritable(1);
	private static int mapNumber = 0;

	@Override
	protected void setup(Context context) {
		this.mapNumber++;
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": Start");
	}

	// map(A, B, Context), A: LongWritable and B: Text if sample.input = file(s)
	@Override
	public void map(LongWritable longWritable, Text text, Context context) throws IOException, InterruptedException {
		String line = text.toString();
		line = line.replaceAll("[^a-zA-Z0-9\\s]", "");
		line = line.toLowerCase();
		String[] words = line.split(" ");
		// context.write(C, D);
		for (String word : words) {
			if (word.length() > 0) {
				context.write(new Text(word), ONE);
			}
		}
	}

	@Override
	protected void cleanup(Context context) {
		System.out.println("Map" + String.valueOf(this.mapNumber) + ": End");
	}

}
