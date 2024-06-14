package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Properties properties = new Properties();
    private static String filePath = "src/resources/jdbc.properties";
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    private static String logFile;

    static {
        FileReader in = null;
        try {
            in = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File is not found: " + filePath);
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            System.out.println("Problem with IO: " + e.getMessage());
            e.printStackTrace();
        }

        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        driver = properties.getProperty("driver");
        logFile = properties.getProperty("logFile");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver is not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (logFile != null) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(logFile);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
            DriverManager.setLogWriter(writer);
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("I'm here: " + e.getMessage());
        }
        return connection;
    }

/*    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println(conn);
        }
        else
            System.out.println("No connection!");
        }*/

}
