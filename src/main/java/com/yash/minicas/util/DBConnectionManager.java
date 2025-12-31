package com.yash.minicas.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionManager {
    private static final Properties props = new Properties();

    static {
        try (InputStream inputStream = DBConnectionManager.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            props.load(inputStream);

            Class.forName(props.getProperty("oracleDriver"));

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to initialize DBConnectionManager", e);
        }
    }

    private DBConnectionManager() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(props.getProperty("url"), props.getProperty("user"), props.getProperty("password"));
    }
}
