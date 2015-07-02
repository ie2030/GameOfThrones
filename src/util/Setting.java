package util;

public class Setting {
    public final static String driverName = "com.mysql.jdbc.Driver";   // driver
    public final static String username = "root";                      // user 
    public final static String password = "root";                      // password
    public final static String host = "localhost";                     // host   
    public final static String database = "gameofthrones";             // database
    public final static String port = "3306";                          // port MySQL
    public final static String url = "jdbc:mysql://" + host + ":" + port + "/" + database;   // URL for MySQL   
}
