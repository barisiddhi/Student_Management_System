package com.studentmgmt.util;

import com.studentmgmt.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "MySQL JDBC driver not found. Place mysql-connector-j.jar in the lib folder.");
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
    }
}
