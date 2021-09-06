package com.pumahawk.rest.db.bridge.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pumahawk.rest.db.bridge.config.DatabaseConnectionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectTableService {

    private static final Logger log = LoggerFactory.getLogger(SelectTableService.class);

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    public List<JsonNode> selectFromTable(String databaseName, String table, String where, Integer limit, Integer skip) {
 
        Connection connection = null;
        String limitSql = "OFFSET " + (skip != null ? skip.toString() : "0") + " ROWS FETCH NEXT " + (limit != null ? limit.toString() : "50") + " ROWS ONLY";
        String whereSql = where != null ? "WHERE " + where : "";
        String sql = "SELECT * from " + table + " " + whereSql + " " + limitSql;
        try {
            DataSource dataSource = databaseConnectionService.getDatasource(databaseName);
            if (dataSource != null) {
                connection = dataSource.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                ResultSetMetaData  meta = resultSet.getMetaData();
                int columnCount = meta.getColumnCount();
                List<JsonNode> records = new LinkedList<>();
                while(resultSet.next()) {
                    ObjectNode on = new ObjectMapper().createObjectNode();
                    for (int i = 1; i <= columnCount; i++) {
                        switch(meta.getColumnType(i)) {
                            case Types.BLOB:
                            case Types.CLOB:
                                on.put(meta.getColumnName(i), Base64.getEncoder().encodeToString(resultSet.getBytes(i)));
                                break;
                            default:
                                on.put(meta.getColumnName(i), String.valueOf(resultSet.getObject(i)));
                        }
                    }
                    records.add(on);
                }
                return records;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("query: {}", sql);
            log.error("Unable to retrieve tables for database: {}, table: {}", databaseName, table, e);
            return Collections.emptyList();
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
