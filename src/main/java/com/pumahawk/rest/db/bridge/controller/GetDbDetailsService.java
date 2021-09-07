package com.pumahawk.rest.db.bridge.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.pumahawk.rest.db.bridge.config.DatabaseConnectionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDbDetailsService {

    private static final Logger log = LoggerFactory.getLogger(GetDbDetailsService.class);

    @Autowired
    public DatabaseConnectionService databaseConnectionService;

    public List<String> getDatabaseDetails(String databaseName) {
        Connection connection = null;
        try {
            DataSource dataSource = databaseConnectionService.getDatasource(databaseName);
            if (dataSource != null) {
                String sql = "SELECT table_name FROM all_tables";
                connection = dataSource.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
    
                List<String> tables = new LinkedList<>();
                while(resultSet.next()) {
                    tables.add(resultSet.getString(1));
                }
                return tables;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Unable to retrieve tables for database: {}", databaseName, e);
            throw new RuntimeException("Unable to Get DB details", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.error("Unable to close connection", e);
                }
            }
        }
    }

}
