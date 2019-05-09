package com.senko.movieRatings;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

public class MovireRatingsSortDriver extends Configured implements Tool {

	public int run(String[] args) throws Exception {

		Path inputDirPath = new Path("src/main/resources/input/movieratings/");
		Path outputDirPath = new Path("src/main/resources/temp/");

		Path inputDirPath2 = new Path("src/main/resources/temp/");
		Path outputDirPath2 = new Path("src/main/resources/output/movieratings/");

		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "file:/");
		conf.set("mapreduce.framework.name", "local");
		FileSystem fs = FileSystem.getLocal(conf);
		fs.delete(outputDirPath, true);
		fs.delete(outputDirPath2, true);
		fs.setWriteChecksum(false);

		JobControl jobControl = new JobControl("jobChain");
		Configuration conf1 = getConf();

		Job job1 = Job.getInstance(conf1);
		job1.setJarByClass(MovireRatingsSortDriver.class);
		job1.setJobName("MovieRatings");

		FileInputFormat.addInputPath(job1, inputDirPath);
		FileOutputFormat.setOutputPath(job1, outputDirPath);

		job1.setMapperClass(MovieRatingsMapper.class);
		job1.setCombinerClass(MovieRatingsReducer.class);
		job1.setNumReduceTasks(1);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);

		ControlledJob controlledJob1 = new ControlledJob(job1.getConfiguration());
		controlledJob1.setJob(job1);

		jobControl.addJob(controlledJob1);

		Configuration conf2 = getConf();

		Job job2 = Job.getInstance(conf2);
		job2.setJarByClass(MovireRatingsSortDriver.class);
		job2.setJobName("Sorter");

		FileInputFormat.addInputPath(job2, inputDirPath2);
		FileOutputFormat.setOutputPath(job2, outputDirPath2);

		job2.setMapperClass(MovieRatingsSortMapper.class);
		job2.setReducerClass(MovieRatingsSortReducer.class);

		job2.setOutputKeyClass(IntWritable.class);
		job2.setOutputValueClass(Text.class);
		job2.setInputFormatClass(KeyValueTextInputFormat.class);	 
		
		job2.setNumReduceTasks(1);

		ControlledJob controlledJob2 = new ControlledJob(job2.getConfiguration());
		controlledJob2.setJob(job2);

		// make job2 dependent on job1
		controlledJob2.addDependingJob(controlledJob1);

		// add the job to the job control
		jobControl.addJob(controlledJob2);

		Thread jobControlThread = new Thread(jobControl);
		jobControlThread.start();

		while (!jobControl.allFinished()){
			System.out.println("all finished: " + jobControl.allFinished());
			Thread.sleep(500);
		}
		
		System.out.println("outside while all finished: " + jobControl.allFinished());
		jobControl.stop();
		return 0;

		

	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new MovireRatingsSortDriver(), args);
		System.exit(exitCode);

	}

}
