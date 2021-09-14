package com.pumahawk.rest.db.bridge.controller;

import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.pumahawk.rest.db.bridge.config.DatabaseConnectionService;
import com.pumahawk.rest.db.bridge.dto.UpdateTableResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class UpdateTableService {

    private static final Logger log = LoggerFactory.getLogger(UpdateTableService.class);

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    public UpdateTableResponse updateTableResponse(String database, String table, MultiValueMap<String, String> request) {
        UpdateTableResponse updateTableResponse = new UpdateTableResponse();

        String sql = "UPDATE " + table + " SET ";

        sql += String.join(", ", request.keySet().stream().map(k -> k + " = '" + request.getFirst(k) + "'").collect(Collectors.toList()));
        
        
        sql += " WHERE " + request.getFirst("w");
        
        DataSource ds = databaseConnectionService.getDatasource(database);
        try {
            ds.getConnection().prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            log.error("Unable to execute update query: " + sql, e);
            throw new RuntimeException("Unable to execute update query: " + sql, e);
        }

        updateTableResponse.setQuery(sql);
        updateTableResponse.setCount("0");

        return updateTableResponse;
    }

}
