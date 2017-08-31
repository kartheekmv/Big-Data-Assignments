package WordCount.WordCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, LongWritable>{

    private Set<String> positiveSet = new HashSet<String>();
    private Set<String> negativeSet = new HashSet<String>();
    
    public void setup(Context context) throws IOException{
    try {
    	Path path_pos = new Path("hdfs://cshadoop1/user/mxk164930/positive-words.txt");//Location of positive-words file in HDFS
    	   FileSystem fileSystem = FileSystem.get(new Configuration());
    	   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(path_pos)));
    	   String pattern;
	          while ((pattern = bufferedReader.readLine()) != null) {
	        	  if(!pattern.startsWith(";"))
	        	  {
	        		  positiveSet.add(pattern);
	        	  }
	          }
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }
    try {
    	Path path_neg = new Path("hdfs://cshadoop1/user/mxk164930/negative-words.txt");//Location of negative-words file in HDFS
    	   FileSystem fileSystem = FileSystem.get(new Configuration());
    	   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(path_neg)));
    	   String pattern;
	          while ((pattern = bufferedReader.readLine()) != null) {
	        	  if(!pattern.startsWith(";"))
	        	  {
	        		  negativeSet.add(pattern);
	        	  }
	          }
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }
    }

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    
      StringTokenizer itr = new StringTokenizer(value.toString());
      
      while (itr.hasMoreTokens()) {
    	  //String word = itr.nextToken();  
    	  String word = itr.nextToken().toLowerCase().replaceAll("\\s*\\p{Punct}+\\s*$", "");  //Removes punctuation marks at the end of the word
    	  if(negativeSet.contains(word))
    	  {
  			context.write(new Text("negative"),new LongWritable(1));
    	  }
  		  else if(positiveSet.contains(word))
  		  {
  			context.write(new Text("positive"),new LongWritable(1));
  		  }
        
        
      }
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,LongWritable,Text,LongWritable> {
    private LongWritable result = new LongWritable();

    public void reduce(Text key, Iterable<LongWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (LongWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }
  
  

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("mapred.job.tracker", "hdfs://cshadoop1:61120");
    conf.set("yarn.resourcemanager.address", "cshadoop1.utdallas.edu:8032");
    conf.set("mapreduce.framework.name", "yarn");
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}