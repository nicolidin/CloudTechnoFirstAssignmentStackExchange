package example

import java.sql.DriverManager

fun main(args: Array<String>) {
    println("WESH SALUT")
    HiveQueriesScheduler()
}

class HiveQueriesScheduler {
    companion object {
        @JvmStatic
        private var driverName = "org.apache.hive.jdbc.HiveDriver";
    }

    constructor() {
        // Register driver and create driver instance
        Class.forName(driverName);

        // get connection
        val con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "", "")

        // create statement
        val stmt = con.createStatement();

        // execute statement
        val rs = stmt.executeQuery(
            """SELECT * FROM question_post22 ORDER BY Score DESC LIMIT 10"""
        )

        var idx = 0

        while (rs.next()) {
            println(rs.getString("Title"));
            println(rs.getString("Score"));
            idx++
        }
        println(idx)

        con.close();
    }
}
