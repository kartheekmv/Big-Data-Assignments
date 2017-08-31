package WordCount.WordCount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PartsOfSpeech {

  public static class TokenizerMapper
       extends Mapper<LongWritable, Text, Text, CompositeWritable>{
	  
	  private static MaxentTagger tagger = null;
	  private HashMap<String,String> PoSMap = new HashMap<String,String>();
    
      public void setup(Context context) throws IOException{
    	Configuration config = context.getConfiguration();
    	
	      tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	      
	      PoSMap.put("NN","NOUN");             // Noun, singular or mass
	      PoSMap.put("NNS","NOUN");            // Noun, plural
	      PoSMap.put("NNP","NOUN");			   // Proper noun, singular
	      PoSMap.put("NNPS","NOUN");		   // Proper noun, plural
	      
	      PoSMap.put("PRP","PRONOUN");         // Personal pronoun
	      PoSMap.put("PRP$","PRONOUN");        // Possessive pronoun
	      
	      PoSMap.put("JJ","ADJECTIVE");        // Adjective
	      PoSMap.put("JJR","ADJECTIVE");       // Adjective, comparative
	      PoSMap.put("JJS","ADJECTIVE");       // Adjective, superlative
	      
	      PoSMap.put("VB","VERB");             // Verb, base form
	      PoSMap.put("VBD","VERB");            // Verb, past tense
	      PoSMap.put("VBG","VERB");            // Verb, gerund or present participle
	      PoSMap.put("VBN","VERB");            // Verb, past participle
	      PoSMap.put("VBP","VERB");            // Verb, non-3rd person singular present
	      PoSMap.put("VBZ","VERB");            // Verb, 3rd person singular present
	      
	      PoSMap.put("RB","ADVERB");           // Adverb
	      PoSMap.put("RBR","ADVERB"); 		   // Adverb, comparative
	      PoSMap.put("RBS","ADVERB");          // Adverb, superlative
	      
	      PoSMap.put("IN","CONJUNCTION");      // Preposition or subordinating conjunction
	      
	      PoSMap.put("UH","INTERJECTION");      // Interjection
    
      }

      public void map(LongWritable offset, Text value, Context context)
	        throws IOException, InterruptedException {
	      
	      boolean is_Palindrome = false;
	      int word_length = 0;
	      String pos_found;
	      
	      StringTokenizer itr = new StringTokenizer(value.toString());
	      
	      while (itr.hasMoreTokens()) {
	    	  //String word = itr.nextToken();  
	    	  String word = itr.nextToken().toLowerCase().replaceAll("\\s*\\p{Punct}+\\s*$", "");  //Removes punctuation marks at the end of the word
	    	  if(word.length() >= 5)
	    	  {
	    		  word_length = word.length();
	    		  is_Palindrome = isPalindrome(word);
	    		  word = this.tagger.tagTokenizedString(word);
	    
	    		  word = (word.split("_")[1]).trim();
	    		  
	    		  pos_found = parts_of_speech(word);
	    		  
	    		  if(pos_found != "NO"){
	    		  context.write(new Text(Integer.toString(word_length)),new CompositeWritable((pos_found),is_Palindrome));
	    		  }
	    	  }
	      }
	      }
	      
	      private String parts_of_speech(String string) {
				if(this.PoSMap.containsKey(string))
				{
					return this.PoSMap.get(string);
				}
				return "NO";
			}
	      
		  private boolean isPalindrome(String word) {
				StringBuffer sb = new StringBuffer(word);
				if(word.equals(sb.reverse().toString()))
						{
							return true;
							
						}
				return false;
		  }
  }

  
  public static class Reduce extends Reducer<Text, CompositeWritable, Text, Text> {
	    @Override
	    public void reduce(Text word, Iterable<CompositeWritable> count, Context context)
	        throws IOException, InterruptedException {
	      long total_words = 0L;
	      HashMap<String, Long> PoSCount = new HashMap<String,Long>();
	      long palindromeCount = 0L;
	      for (CompositeWritable obj : count) {
	    	  
	    	  if(obj.Palindrome)
	    	  {
	    		  palindromeCount++;
	    	  }
	    	  if(PoSCount.containsKey(obj.PartsofSpeech))
	    	  {
	    		  PoSCount.put(obj.PartsofSpeech, (PoSCount.get(obj.PartsofSpeech) + 1));
	    	  }
	    	  else
	    	  {
	    		  PoSCount.put(obj.PartsofSpeech, 1L);
	    	  }
	    	  total_words++;
	      }
	      
	      context.write(new Text("\nLength : "), new Text(word));
	      context.write(new Text("Count of Words : "), new Text(Long.toString(total_words)));
	      
	      StringBuilder sb = new StringBuilder();
	      for(String pos : PoSCount.keySet())
	      {
	    	  sb.append(pos).append(": ").append(PoSCount.get(pos)).append("; ");
	      }
	      sb.append("}");
	      context.write(new Text("Distribution of POS: {"), new Text(sb.toString()));
	      context.write(new Text("Number of palindromes : "), new Text(Long.toString(palindromeCount)));
	      
	    }
	  }
	  
  private static class CompositeWritable implements Writable{
		private String PartsofSpeech;
		private boolean Palindrome;
		
		public CompositeWritable(){
			  
		}
		
		public CompositeWritable(String PartsofSpeech, boolean Palindrome) {
			this.PartsofSpeech = PartsofSpeech;
			this.Palindrome = Palindrome;
		}
		public void readFields(DataInput in) throws IOException {
			this.Palindrome = in.readBoolean();
			this.PartsofSpeech = WritableUtils.readString(in);
			
		}
		public void write(DataOutput out) throws IOException {
			
			out.writeBoolean(Palindrome);
			WritableUtils.writeString(out, PartsofSpeech);
		}

		@Override
		public String toString() {
			return "CompositeWritable [PartsofSpeech=" + PartsofSpeech + ", Palindrome=" + Palindrome + "]";
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
    job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(CompositeWritable.class);
    job.setReducerClass(Reduce.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
