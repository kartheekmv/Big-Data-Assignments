Steps for execution of part-1 of assignement-1b 


1) Unzip the WordCount.zip and Import the "WordCount" project as "Existing Maven Project" in Eclipse EE.

2) Build the Project by right clicking on pom.xml and then Run As -> Maven build. If
it asks for "goal", enter "package".

3) If it builds successfully, you will get two jar files in the "target" folder of your project. You can
then upload the "WordCount-jar-with-dependencies.jar" to the cluster using either a FTP client or scp command or simply copy and paste the 
jar file in cluster with help of eclipse's Remote System Explorer. 
(i.e., copy the file from Local to Remote Connection (ex:- csgrads1.utdallas.edu or cs6360.utdallas.edu))

NOTE: copy the positive-words.txt and negative-words.txt (given in WordCount file) to hadoop cluster if they don't exist on /user/mxk164930/

4) Run the jar file on the cluster by the following command:

hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar WordCount-jar-with-dependencies.jar WordCount.WordCount.WordCount /user/mxk164930/assignment1/part1 /user/mxk164930/assignment1b/part1


5) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
" hdfs dfs -ls /user/mxk164930/assignment1b/part1 "

6) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
" hdfs dfs -cat /user/mxk164930/assignment1b/part1/<outputtedFileName> " 