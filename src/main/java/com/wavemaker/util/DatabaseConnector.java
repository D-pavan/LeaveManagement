package com.wavemaker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static DatabaseConnector databaseRepository;
    private Connection connection;

    private DatabaseConnector() {
        Properties properties = new Properties();
        Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
        try {
            try (InputStream inputStream = DatabaseConnector.class.getClassLoader().getResourceAsStream("database/database.properties")) {
                if (inputStream == null) {
                    logger.info("Unable to find the database properties file.");
                }
                properties.load(inputStream);

            } catch (IOException ioException) {
                String exception = ioException.getMessage();
                logger.error("Unable to load the database properties file");
                logger.error("Error : {}", ioException.getMessage());
            }
            Class.forName(properties.getProperty("db.driver"));
            this.connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );

        } catch (ClassNotFoundException | SQLException exception) {
            logger.error("exception occurred : {}", exception.getMessage());
        }

    }

    public static synchronized DatabaseConnector getInstance() {
        if (databaseRepository == null) {
            databaseRepository = new DatabaseConnector();
        }
        return databaseRepository;
    }

    public Connection getConnection() {
        return connection;
    }
}
