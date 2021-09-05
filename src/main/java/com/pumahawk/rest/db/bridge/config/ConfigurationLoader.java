package com.pumahawk.rest.db.bridge.config;

import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pumahawk.rest.db.bridge.model.configuration.ApplicationConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationLoader {

    @Value("${config-file}:configuration.json")
    String configurationPath;

    @Bean
    public ApplicationConfiguration serverConfiguration() throws JsonParseException, JsonMappingException, IOException {
        return new ObjectMapper().readValue(Paths.get(configurationPath).toFile(), ApplicationConfiguration.class);
    }
}
