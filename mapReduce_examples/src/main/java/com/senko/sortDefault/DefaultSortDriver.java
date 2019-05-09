package com.senko.sortDefault;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.senko.wordcount.WordCountMapper;
import com.senko.wordcount.WordCountReducer;

public class DefaultSortDriver {
	
	public static void main(String[] args) throws Exception {
		Path inputDirPath = new Path("src/main/resources/input/defaultSort/");
	    Path outputDirPath = new Path("src/main/resources/output/defaultSort/");
	    Configuration conf = new Configuration();
	    conf.set("fs.defaultFS", "file:/");
        conf.set("mapreduce.framework.name", "local");
        FileSystem fs = FileSystem.getLocal(conf);
        fs.delete(outputDirPath, true);
        fs.setWriteChecksum(false);
        
        Job job = Job.getInstance(conf, "default sort");
        job.setMapperClass(DefaultSortMapper.class);
        job.setReducerClass(DefaultSortReducer.class);
        
        job.setNumReduceTasks(1);
        
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, inputDirPath);
        FileOutputFormat.setOutputPath(job, outputDirPath);
        
        System.out.println("so far everything is ok");
        System.exit(job.waitForCompletion(true) ? 0 : 1);
        
	}

}
