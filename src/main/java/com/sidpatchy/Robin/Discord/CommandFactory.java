package com.sidpatchy.Robin.Discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;

public class CommandFactory {

    /**
     * Loads a YAML configuration file and instantiates an object of the specified type.
     * This method is specifically tailored for creating command configurations.
     *
     * @param filePath    The path to the YAML configuration file.
     * @param configClass The class type of the configuration object to create.
     * @param <T>         The type of the configuration object.
     * @return An instance of the specified class filled with data from the YAML file.
     */
    public static <T> T loadConfig(String filePath, Class<T> configClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(new File(filePath), configClass);
    }
}
