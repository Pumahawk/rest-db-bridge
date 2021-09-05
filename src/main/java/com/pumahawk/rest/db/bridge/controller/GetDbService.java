package com.pumahawk.rest.db.bridge.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.pumahawk.rest.db.bridge.config.DatabaseConnectionService;
import com.pumahawk.rest.db.bridge.model.core.DatabaseInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDbService {

    @Autowired
    public DatabaseConnectionService databaseConnectionService;

    public List<DatabaseInformation> getRootApi() {
        return databaseConnectionService
            .getDatabaseList()
            .stream()
            .map(name ->  new DatabaseInformation(){{
                    setName(name);
                }}
            ).collect(Collectors.toList());
    }

}
