package com.senko.movieRatings;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.tools.GetConf;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieRatingsDriver {
	
	public static void main(String[] args) throws Exception {
		Path inputDirPath = new Path("src/main/resources/input/movieratings/");
        Path outputDirPath = new Path("src/main/resources/output/movieratings/");
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "file:/");
        conf.set("mapreduce.framework.name", "local");
        FileSystem fs = FileSystem.getLocal(conf);
        fs.delete(outputDirPath, true);
        fs.setWriteChecksum(false);
    
        Job job = Job.getInstance(conf, "Word count");
        job.setMapperClass(MovieRatingsMapper.class);
        job.setReducerClass(MovieRatingsReducer.class);
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        
        FileInputFormat.addInputPath(job, inputDirPath);
        FileOutputFormat.setOutputPath(job, outputDirPath);
        System.out.println("so far everything is ok");
        System.exit(job.waitForCompletion(true) ? 0 : 1);
       
        
	}

}
