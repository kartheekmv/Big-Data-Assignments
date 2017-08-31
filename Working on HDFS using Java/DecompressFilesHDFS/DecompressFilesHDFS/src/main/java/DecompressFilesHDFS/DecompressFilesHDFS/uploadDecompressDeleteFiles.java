package DecompressFilesHDFS.DecompressFilesHDFS;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.util.Progressable;

public class uploadDecompressDeleteFiles {
	public static void main(String[] args) throws Exception{
		// Downloading the mentioned books from the links given
		// First we will store links and file names in Hashmap
		
		Map<String, String> url_links = new HashMap<String, String>();
		url_links.put("20417","http://www.utdallas.edu/~axn112530/cs6350/lab2/input/20417.txt.bz2");
		url_links.put("5000-8","http://www.utdallas.edu/~axn112530/cs6350/lab2/input/5000-8.txt.bz2");
		url_links.put("132","http://www.utdallas.edu/~axn112530/cs6350/lab2/input/132.txt.bz2");
		url_links.put("1661-8","http://www.utdallas.edu/~axn112530/cs6350/lab2/input/1661-8.txt.bz2");
		url_links.put("972","http://www.utdallas.edu/~axn112530/cs6350/lab2/input/972.txt.bz2");
		url_links.put("19699","http://www.utdallas.edu/~axn112530/cs6350/lab2/input/19699.txt.bz2");
		
		String destination = args[0];
		
		InputStream in = null;
		InputStream in1 =null;
		OutputStream out = null;
		OutputStream out1 = null;
		FileSystem fs = null;
		 
		try{
			for(String url_link : url_links.keySet())
		    {
		    	URL link = new URL(url_links.get(url_link));
				in = new BufferedInputStream(link.openStream());
				
				Configuration conf = new Configuration();
			    conf.addResource(new Path("/usr/local/hadoop-2.4.1/etc/hadoop/core-site.xml"));
			    conf.addResource(new Path("/usr/local/hadoop-2.4.1/etc/hadoop/hdfs-site.xml"));
			    
			    String urlString = destination + url_link + ".txt.bz2";
			    
			    // Uploading compressed (.bz2) files to hadoop cluster
			    
			    fs = FileSystem.get(URI.create(urlString), conf);
			    out = fs.create(new Path(urlString), new Progressable() {
			      public void progress() {  // Shows the progress of uploading
			        System.out.print(".");
			      }
			    });
			    IOUtils.copyBytes(in, out, 4096, true);
			    
			    // Decompressing the file on HDFS file system
			    
			    Path inputPath = new Path(urlString);
			    CompressionCodecFactory factory = new CompressionCodecFactory(conf);
			    CompressionCodec codec = factory.getCodec(inputPath);
			    if (codec == null) {
			      System.err.println("No codec found for " + urlString);
			      System.exit(1);
			    }			    
			      String outputUri =
			      CompressionCodecFactory.removeSuffix(urlString, codec.getDefaultExtension());    
			      in1 = codec.createInputStream(fs.open(inputPath));
			      out1 = fs.create(new Path(outputUri));
			      IOUtils.copyBytes(in1, out1, conf);
			      
			      // Deleting the uploaded compressed (.bz2) files from HDFS file system
			      
			      fs.delete(inputPath);
		    }
		}finally{
			IOUtils.closeStream(in);
		    IOUtils.closeStream(out);
		    IOUtils.closeStream(in1);
		    IOUtils.closeStream(out1);
			
		}
		
	}

}
