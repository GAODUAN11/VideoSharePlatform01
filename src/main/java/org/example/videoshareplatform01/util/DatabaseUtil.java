package org.example.videoshareplatform01.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = ConfigUtil.getProperty("db.url", "jdbc:mysql://localhost:3306/video_share?useSSL=false&serverTimezone=UTC");
    private static final String USERNAME = ConfigUtil.getProperty("db.username", "root");
    private static final String PASSWORD = ConfigUtil.getProperty("db.password", "password");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}