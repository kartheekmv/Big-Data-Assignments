Steps for execution of assignement-2 

The dataset files are located in HDFS in the following path,
/yelp/business/business.csv
/yelp/review/review.csv
/yelp/user/user.csv
If somehow the files disappear from the above HDFS location, you can also download	
them from:
http://www.utdallas.edu/~axn112530/cs6350/yelp/

1) Unzip the Yelp.zip and Import the "Yelp" project as "Existing Maven Project" in Eclipse EE.

2) Build the Project by right clicking on pom.xml and then Run As -> Maven build. If
it asks for "goal", enter "package".

3) If it builds successfully, you will get two jar files in the "target" folder of your project. You can
then upload the "WordCount-jar-with-dependencies.jar" to the cluster using either a FTP client or scp command or simply copy and paste the 
jar file in cluster with help of eclipse's Remote System Explorer. 
(i.e., copy the file from Local to Remote Connection (ex:- csgrads1.utdallas.edu or cs6360.utdallas.edu))

Question 1:

4) Run the jar file on the cluster by the following command:

hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar yelp.jar CountYelpBusiness /yelp/business/business.csv /user/mxk164930/assignment2/q1

5) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
Ex:- " hdfs dfs -ls /user/mxk164930/assignment2/q1 "

6) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
Ex:- " hdfs dfs -cat /user/mxk164930/assignment2/q1/<outputtedFileName> "


Question 2:

4) Run the jar file on the cluster by the following command:
hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar yelp.jar AddressYelpBusiness /yelp/business/business.csv /user/mxk164930/assignment2/q2

5) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
Ex:- " hdfs dfs -ls /user/mxk164930/assignment2/q2 "

6) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
Ex:- " hdfs dfs -cat /user/mxk164930/assignment2/q2/<outputtedFileName> "


Question 3:

4) Run the jar file on the cluster by the following command:
hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar yelp.jar TopNYelpBusiness /yelp/business/business.csv /user/mxk164930/assignment2/q3

5) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
Ex:- " hdfs dfs -ls /user/mxk164930/assignment2/q3 "

6) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
Ex:- " hdfs dfs -cat /user/mxk164930/assignment2/q3/<outputtedFileName> "


Question 4:

4) Run the jar file on the cluster by the following command:
hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar yelp.jar TopRatedNYelpBusiness /yelp/review/review.csv /user/mxk164930/assignment2/q4

5) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
Ex:- " hdfs dfs -ls /user/mxk164930/assignment2/q4 "

6) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
Ex:- " hdfs dfs -cat /user/mxk164930/assignment2/q4/<outputtedFileName> "


Question 5:

4) Run the jar file on the cluster by the following command:
hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar yelp.jar LowRatedNYelpBusiness /yelp/review/review.csv /user/mxk164930/assignment2/q5

5) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
Ex:- " hdfs dfs -ls /user/mxk164930/assignment2/q5 "

6) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
Ex:- " hdfs dfs -cat /user/mxk164930/assignment2/q5/<outputtedFileName> "


