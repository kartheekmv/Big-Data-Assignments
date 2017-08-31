Steps for execution of part-2 of assignement-1b 

1) Use the same jar file "WordCount-jar-with-dependencies.jar" and Run the following command:

hadoop jar <jarfile> <ClassName> <input path> <output path>

Ex:- hadoop jar WordCount-jar-with-dependencies.jar WordCount.WordCount.PartsOfSpeech /user/mxk164930/assignment1/part2 /user/mxk164930/assignment1b/part2


2) Enter the following command " hdfs dfs -ls <output path> " to see the name of outputtedFileName.
" hdfs dfs -ls /user/mxk164930/assignment1b/part2 "

3) Enter the following command " hdfs dfs -cat <output path>/<outputtedFileName> " to see the output of the program.
" hdfs dfs -cat /user/mxk164930/assignment1b/part2/<outputtedFileName> " 