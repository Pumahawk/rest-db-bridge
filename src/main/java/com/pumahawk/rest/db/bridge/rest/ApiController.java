package com.pumahawk.rest.db.bridge.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ApiController {

    @GetMapping("/db")
    public ResponseEntity<Void> getRootApi() {
        return null;
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
