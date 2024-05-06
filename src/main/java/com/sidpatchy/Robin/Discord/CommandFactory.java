package com.sidpatchy.Robin.Discord;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CommandFactory {

    /**
     * Loads a YAML configuration file and instantiates an object of the specified type.
     * This method is specifically tailored for creating command configurations.
     *
     * @param filePath The path to the YAML configuration file.
     * @param configClass The class type of the configuration object to create.
     * @return An instance of the specified class filled with data from the YAML file.
     * @throws FileNotFoundException if the specified file does not exist.
     */
    public static <T> T loadConfig(String filePath, Class<T> configClass) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        return yaml.loadAs(inputStream, configClass);
    }
}
