package com.senko.grep;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class GrepDriver {
	
	public static void main(String[] args) throws IOException, Exception, InterruptedException {
		
		Path inputDirPath = new Path("src/main/resources/input/grep/");
        Path outputDirPath = new Path("src/main/resources/output/grep/");
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "file:/");
	    conf.set("mapreduce.framework.name", "local");  
		conf.set(GrepMapper.PATTERN, "abc"); 
		
		System.out.println(conf.get(GrepMapper.PATTERN));
		
		FileSystem fs = FileSystem.getLocal(conf);
		fs.delete(outputDirPath, true);
        fs.setWriteChecksum(false);
		
		Job job = Job.getInstance(conf);
		job.setJobName("grep task");
		job.setMapperClass(GrepMapper.class);
        job.setReducerClass(GrepReducer.class);
        job.setNumReduceTasks(1);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        
        FileInputFormat.addInputPath(job, inputDirPath);
        FileOutputFormat.setOutputPath(job, outputDirPath);
        System.out.println("so far everything is ok");
        System.exit(job.waitForCompletion(true) ? 0 : 1);
        
	}


}
