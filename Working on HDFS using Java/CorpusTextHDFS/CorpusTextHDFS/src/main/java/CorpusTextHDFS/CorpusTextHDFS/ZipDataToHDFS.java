package CorpusTextHDFS.CorpusTextHDFS;

import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class ZipDataToHDFS {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			 
		    String destination = args[0];
		    
		    ZipInputStream zis = null;
		    OutputStream out = null;
		    FileSystem fs = null;
		    try
		    {
		    String url_link = "http://corpus.byu.edu/cocatext/samples/text.zip";
		    
		    Configuration conf = new Configuration();
		    conf.addResource(new Path("/usr/local/hadoop-2.4.1/etc/hadoop/core-site.xml"));
		    conf.addResource(new Path("/usr/local/hadoop-2.4.1/etc/hadoop/hdfs-site.xml"));
		    
		    // Reading the zip file using zipInputStream
		    URL link = new URL(url_link);
			zis = new ZipInputStream(link.openStream());
				ZipEntry ze = null;
				
				// Getting data of zip file to Hadoop Cluster
				
				while ((ze = zis.getNextEntry()) != null) {
					String StringUrl = destination + ze.getName();
					fs = FileSystem.get(URI.create(StringUrl), conf);
					out = fs.create(new Path(StringUrl), new Progressable() {
						public void progress() { // Shows the progress of uploading data
						   System.out.print(".");
							}
						});
					
					IOUtils.copyBytes(zis, out, 4096);
					zis.closeEntry();
					IOUtils.closeStream(out);
				  }
			}
		    finally {
		      IOUtils.closeStream(zis);
		    }

	}

}
