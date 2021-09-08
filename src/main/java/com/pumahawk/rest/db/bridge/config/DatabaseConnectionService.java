package com.pumahawk.rest.db.bridge.config;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.pumahawk.rest.db.bridge.model.configuration.ApplicationConfiguration;
import com.pumahawk.rest.db.bridge.model.configuration.ConnectionParameter;
import com.pumahawk.rest.db.bridge.model.configuration.DatabaseConnection;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConnectionService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionService.class);

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    private Map<String, Supplier<DataSource>> connections = new HashMap<>();

    @PostConstruct
    private void satabaseConnectionServiceInit() {
        List<DatabaseConnection>  connectinosDatabase = applicationConfiguration.getDatabaseConections();
        if (connectinosDatabase.isEmpty()) {
            log.warn("No database connection found");
        } else {
            for (DatabaseConnection connection : connectinosDatabase) {
                try {
                    Supplier<DataSource> dataSource = () -> {
                        try {
                            return getDatasourceFromConfig(connection.getConnectionParameter());
                        } catch (Exception e) {
                            throw new RuntimeException("unable to get datasource", e);
                        }
                    };
                    storeConnection(connection.getName(), dataSource);
                    for (String alias : connection.getAlias()) {
                        storeConnection(alias, dataSource);
                    }
                } catch (Exception e) {
                    log.error("Unable to retrieve connection. Name: {}", connection.getName(), e);
                }
            }
        }

    }

    private void storeConnection(String name, Supplier<DataSource> connection) {
        AbstractMap.SimpleEntry<String, DataSource> ds = new AbstractMap.SimpleEntry<String, DataSource>(null, null);
        connections.put(name, () -> {
            if (ds.getValue() != null) {
                return ds.getValue();
            } else {
                try {
                    DataSource d = connection.get();
                    ds.setValue(d);
                    return d;
                } catch (Exception e) {
                    log.error("Error connection. name: " + name);
                    throw new RuntimeException("Unable to get ex", e);
                }
            }
        });
        log.info("Register database connection. Name: {}", name);
    }

    private DataSource getDatasourceFromConfig(ConnectionParameter connection) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connection.getUrl());
        config.setDriverClassName(connection.getDriverClass());
        config.setUsername(connection.getUsername());
        config.setPassword(connection.getPassword());
        return new HikariDataSource(config);
    }

    public DataSource getDatasource(String dbName) {
        return connections.get(dbName).get();
    }

    public Set<String> getDatabaseList() {
        return connections.keySet();
    }
    
}
