package com.pumahawk.rest.db.bridge.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.pumahawk.rest.db.bridge.controller.GetDbService;
import com.pumahawk.rest.db.bridge.dto.Database;
import com.pumahawk.rest.db.bridge.dto.GetDBResponse;

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
    public ResponseEntity<Void> getDatabase(
        @PathVariable("dbName") String dbName) {
        return null;
    }

    @GetMapping("/db/{dbName}/_select")
    public ResponseEntity<Void> dBSelect(
        @PathVariable("dbName") String dbName,
        @RequestParam("c") String colums,
        @RequestParam("f") String from,
        @RequestParam("w") String where,
        @RequestParam("l") int limit,
        @RequestParam("s") int skip) {
        return null;
    }

    @GetMapping("/db/{dbName}/{tableName}")
    public ResponseEntity<Void> getTable(
        @PathVariable("dbName") String dbName,
        @PathVariable("tableName") String tableName) {
        return null;
    }

    @GetMapping("/db/{dbName}/{tableName}/_select")
    public ResponseEntity<Void> getTableSelect(
        @PathVariable("dbName") String dbName,
        @PathVariable("tableName") String tableName,
        @RequestParam("w") String where,
        @RequestParam("l") int limit,
        @RequestParam("s") int skip) {
        return null;
    }

}
