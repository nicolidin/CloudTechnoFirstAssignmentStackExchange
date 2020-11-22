package example

import java.sql.DriverManager

fun main(args: Array<String>) {
    HiveStackExchangeData()
}

class HiveStackExchangeData {
    companion object {
        @JvmStatic
        private var driverName = "org.apache.hive.jdbc.HiveDriver";
    }

    constructor() {
        // Register driver and create driver instance
        Class.forName(driverName);

        // get connection
        // val con = DriverManager.getConnection("jdbc:hive://localhost:10000/userdb", "", "");
        val con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "", "")

        // create statement
        val stmt = con.createStatement();

        stmt.execute(
            """CREATE TABLE IF NOT EXISTS question_post27 (nb String, Id int, PostTypeId int,  AcceptedAnswerId int, ParentId int, CreationDate date, DeletionDate date, Score int, ViewCount int, OwnerUserId int, OwnerDisplayName String, LastEditorUserId int, LastEditorDisplayName String, LastEditDate date, LastActivityDate date, Title String, Tags String, AnswerCount int, CommentCount int, FavoriteCount int, ClosedDate date, CommunityOwnedDate date, ContentLicense string) COMMENT 'question post on stackoverflow' ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",") STORED AS TEXTFILE tblproperties('skip.header.line.count'='1') """
        );
	    stmt.execute("""LOAD DATA INPATH '/tmp/csvHdfsStackExchange/QueryResultsEditedSupTo36590.csv' INTO TABLE question_post27""");
	    stmt.execute("""LOAD DATA INPATH '/tmp/csvHdfsStackExchange/QueryResultsEditedSupTo47039.csv' INTO TABLE question_post27""");
	    stmt.execute("""LOAD DATA INPATH '/tmp/csvHdfsStackExchange/QueryResultsEditedSupTo65887.csv' INTO TABLE question_post27""");
	    stmt.execute("""LOAD DATA INPATH '/tmp/csvHdfsStackExchange/QueryResultsEditedSupTo111930.csv' INTO TABLE question_post27""");
        println("Table created.");

	    val bestPost = stmt.executeQuery("""SELECT Title, Score FROM question_post27 ORDER BY Score + 0 DESC LIMIT 10""");

	    println("---------------3.1 : The top 10 posts by score---------------");
        while (bestPost.next()) {
            print("Title: ${bestPost.getString("Title")} | ");
            println("Score: ${bestPost.getString("Score")}");
        }


	    val topUser = stmt.executeQuery("""SELECT OwnerUserId, SUM(Score + 0) AS TotalScore FROM question_post27 GROUP BY OwnerUserId ORDER BY TotalScore DESC LIMIT 10""");

	    println("\n---------------3.2 : The top 10 users by post score---------------");
        while (topUser.next()) {
            print("OwnerUserId: ${topUser.getString("OwnerUserId")} | ");
            println("TotalScore: ${topUser.getString("TotalScore")}");
        }

	    println("\n---------------3.3 : The number of distinct users, who used the word “Hadoop” in one of their posts---------------");
	    val nbUserUsingHadoop = stmt.executeQuery("""SELECT COUNT(DISTINCT OwnerUserId) FROM question_post27 WHERE (Title LIKE '%hadoop%' or Tags LIKE '%hadoop%')""");
        while (nbUserUsingHadoop.next()) {
            println("nbPostUsingWordHadoop: ${nbUserUsingHadoop.getString(1)}");
        }
	    stmt.close();
            con.close();
        }
}
