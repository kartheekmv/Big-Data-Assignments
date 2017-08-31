Steps for execution of part-2 of assignement-1

==> We used COCA Corpus: http://corpus.byu.edu/cocatext/samples/text.zip

1) Import the "CorpusTextHDFS" project as Existing Maven Project in Eclipse EE.

2) Build the Project by right clicking on pom.xml and then Run As -> Maven build. If
it asks for "goal", enter "package".

3) If it builds successfully, you will get a jar file in the "target" folder of your project. You can
then upload it to the cluster using either a FTP client or scp command or simply copy and paste the 
jar file in cluster with help of eclipse's Remote System Explorer 
(i.e., copy the file from Local to Remote Connection (ex:- csgrads1.utdallas.edu or cs6360.utdallas.edu))

4) Run the jar file on the cluster by the following command:

hadoop jar CorpusTextHDFS-0.0.1-SNAPSHOT.jar CorpusTextHDFS.CorpusTextHDFS.ZipDataToHDFS hdfs://cshadoop1/user/<Your NetID ex:- mxk164930>/assignment1/part2/

5) Enter the following command " hdfs dfs -ls /user/<Your NetID>/assignment1/part2 " to see the coca text files on hadoop cluster.

6) Enter the following command " hdfs dfs -cat /user/<Your NetID>/assignment1/part2/<any file name>.txt " to see the contents of text file.