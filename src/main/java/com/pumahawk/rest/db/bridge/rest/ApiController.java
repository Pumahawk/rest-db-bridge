package com.pumahawk.rest.db.bridge.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.pumahawk.rest.db.bridge.controller.GetDbDetailsService;
import com.pumahawk.rest.db.bridge.controller.GetDbService;
import com.pumahawk.rest.db.bridge.controller.SelectTableService;
import com.pumahawk.rest.db.bridge.dto.Database;
import com.pumahawk.rest.db.bridge.dto.GetDBDetailsResponse;
import com.pumahawk.rest.db.bridge.dto.GetDBResponse;
import com.pumahawk.rest.db.bridge.dto.SelectTableResponse;
import com.pumahawk.rest.db.bridge.dto.SqlConditions;
import com.pumahawk.rest.db.bridge.dto.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ApiController {

    @Autowired
    private GetDbService getDbService;

    @Autowired
    private GetDbDetailsService getDbDetailsService;

    @Autowired
    private SelectTableService selectTableService;

    @GetMapping("/db")
    public ResponseEntity<GetDBResponse> getRootApi() {
        GetDBResponse getDBResponse = new GetDBResponse();

        getDBResponse.setName("Get connection list");
        getDBResponse.setDescription("Get list of connections to database");
        getDBResponse.setRef("/db");
        
        List<Database> databaseList = getDbService
            .getRootApi()
            .stream()
            .map(info -> new Database(){{
                setName(info.getName());
                setRef("/db/" + info.getName());
            }})
            .collect(Collectors.toList());

        getDBResponse.setDatabase(databaseList);

        return ResponseEntity.ok().body(getDBResponse);
    }

    @GetMapping("/db/{dbName}")
    public ResponseEntity<GetDBDetailsResponse> getDatabase(
        @PathVariable("dbName") String dbName) {

        List<String> tables = getDbDetailsService.getDatabaseDetails(dbName);
        if (tables != null) {
            GetDBDetailsResponse response = new GetDBDetailsResponse();
            response.setName(dbName);
            response.setRef("/db/" + dbName);
            response.setTables(tables.stream().map(name -> new Table(){{
                setName(name);
                setRef("/db/" + dbName + "/" + name);
            }}).collect(Collectors.toList()));
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/db/{dbName}/_select")
    public ResponseEntity<SelectTableResponse> dBSelect(
        @PathVariable("dbName") String dbName,
        @RequestParam(value = "c", required = false) String columns,
        @RequestParam(value = "f", required = true) String from,
        @RequestParam(value = "w", required = false) String where,
        @RequestParam(value = "l", required = false, defaultValue = "50") Integer limit,
        @RequestParam(value = "s", required = false) Integer skip) {

            List<JsonNode> records = selectTableService.selectFromTable(dbName, from, columns, where, limit, skip);
    
            SelectTableResponse response = new SelectTableResponse();
            response.setRef("/db/" + dbName + "/_select");
            response.setRecords(records);
    
            SqlConditions sqlConditions = new SqlConditions();
            sqlConditions.setWhere(where);
            sqlConditions.setLimit(limit != null ? limit.toString() : null);
            sqlConditions.setSkip(skip != null ? skip.toString() : null);
            sqlConditions.setColumns(columns);
            sqlConditions.setFrom(from);
            response.setSqlConditions(sqlConditions);
    
            return ResponseEntity.ok().body(response);
    }

    @GetMapping("/db/{dbName}/{tableName}")
    public ResponseEntity<Void> getTable(
        @PathVariable("dbName") String dbName,
        @PathVariable("tableName") String tableName) {
        return null;
    }

    @GetMapping("/db/{dbName}/{tableName}/_select")
    public ResponseEntity<SelectTableResponse> getTableSelect(
        @PathVariable("dbName") String dbName,
        @PathVariable("tableName") String tableName,
        @RequestParam(value = "c", required = false) String columns,
        @RequestParam(value = "w", required = false) String where,
        @RequestParam(value = "l", required = false, defaultValue = "50") Integer limit,
        @RequestParam(value = "s", required = false) Integer skip) {

        List<JsonNode> records = selectTableService.selectFromTable(dbName, tableName, columns, where, limit, skip);

        SelectTableResponse response = new SelectTableResponse();
        response.setRef("/db/" + dbName + "/" + tableName + "/_select");
        response.setRecords(records);

        SqlConditions sqlConditions = new SqlConditions();
        sqlConditions.setWhere(where);
        sqlConditions.setLimit(limit != null ? limit.toString() : null);
        sqlConditions.setSkip(skip != null ? skip.toString() : null);
        sqlConditions.setColumns(columns);
        response.setSqlConditions(sqlConditions);

        return ResponseEntity.ok().body(response);
    }

}
