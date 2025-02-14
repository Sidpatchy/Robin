package com.sidpatchy.Robin.File;

import com.sidpatchy.Robin.Exception.InvalidConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobinConfiguration {
    private static RobinSection data;
    private String fileName;
    private Yaml yaml = new Yaml();
    private static final Logger logger = LogManager.getLogger(RobinConfiguration.class);

    /**
     * Construct a new RobinConfiguration with a file
     * @param fileName the file on the hard drive.
     */
    public RobinConfiguration(String fileName) {
        this.fileName = fileName;

        // Ensure data is initialized
        if (data == null) {
            data = new RobinSection(new HashMap<>());
        }
    }

    /**
     * Construct a new RobinConfiguration without a file
     */
    public RobinConfiguration() {
        this.fileName = null;
    }


    /**
     * Load the config file from the hard drive.
     */
    public void load() throws InvalidConfigurationException {
        if (fileName == null) throw new InvalidConfigurationException("File must be specified");

        try (FileInputStream fis = new FileInputStream(fileName)) {
            data = new RobinSection(yaml.load(fis));
        } catch (IOException | YAMLException e) {
            handleLoadError(e);
        }
    }

    /**
     * Load a YAML from a string.
     *
     * @param contents a valid YAML string.
     * @throws InvalidConfigurationException
     */
    public void loadFromString(String contents) throws InvalidConfigurationException {
        if (contents == null) throw new InvalidConfigurationException("Contents cannot be null");
        data = new RobinSection(yaml.load(contents));
    }

    /**
     * Gets a YAML file from a URL.
     *
     * @param link the link to read from.
     * @throws IOException thrown if unable to access the provided link.
     * @throws InvalidConfigurationException thrown if the YAML file is invalid.
     */
    public void loadFromURL(String link) throws IOException, InvalidConfigurationException {
        URLConnection connection = new URL(link).openConnection();
        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            data = new RobinSection(yaml.load(reader));
        }
    }

    /**
     * Gets a YAML file from a URL where basic authentication is required.
     *
     * @param username the username to authenticate with.
     * @param password the password to authenticate with.
     * @param link the link to read from.
     * @throws IOException thrown if unable to access to provided link.
     * @throws InvalidConfigurationException thrown if the YAML file is invalid.
     */
    public void loadFromURL(String username, String password, String link) throws IOException, InvalidConfigurationException {
        URLConnection connection = new URL(link).openConnection();
        String auth = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        connection.setRequestProperty("Authorization", auth);

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            data = new RobinSection(yaml.load(reader));
        }
    }

    public void saveToFile() throws IOException, InvalidConfigurationException {
        if (fileName == null) throw new InvalidConfigurationException("File must be specified");

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        try (FileWriter writer = new FileWriter(fileName)) {
            new Yaml(options).dump(data.getSectionData(), writer);
        }
    }

    public String saveToString() {
        return yaml.dump(data.getSectionData());
    }

    // Configuration access methods
    public String getString(String parameter) {
        return data.getString(parameter);
    }

    /**
     * Calls RobinSection.getInt() on the root of the YAML file.
     *
     * @param parameter parameter to get from the YAML file.
     * @return value of parameter if present.
     */
    public Integer getInt(String parameter) {
        return data.getInt(parameter);
    }

    /**
     * Calls RobinSection.getFloat() on the root of the YAML file.
     *
     * @param parameter parameter to get from the YAML file.
     * @return value of parameter if present.
     */
    public Float getFloat(String parameter) {
        return data.getFloat(parameter);
    }

    /**
     * Calls RobinSection.getLong() on the root of the YAML file.
     *
     * @param parameter parameter to get from the YAML file.
     * @return value of parameter if present.
     */
    public long getLong(String parameter) {
        return data.getLong(parameter);
    }

    /**
     * Calls RobinSection.getList() on the root of the YAML file.
     *
     * @param parameter parameter to get from the YAML file.
     * @return value of parameter if present.
     */
    public List<Object> getList(String parameter) {
        return data.getList(parameter);
    }

    /**
     * Calls RobinSection.getObj() on the root of the YAML file.
     *
     * @param parameter parameter to get from the YAML file.
     * @return value of parameter if present.
     */
    public Object getObj(String parameter) {
        return data.getObj(parameter);
    }

    /**
     * Calls RobinSection.getSection() on the root of the YAML file.
     *
     * @param sectionName Name of the section in the YAML file.
     * @return value of parameter if present.
     */
    public RobinSection getSection(String sectionName) throws InvalidConfigurationException {
        return data.getSection(sectionName);
    }

    /**
     * Calls RobinSection.set() on the root of the YAML file.
     *
     * @param parameter the path to the object you'd like to update.
     * @param value new value
     */
    public void set(String parameter, Object value) {
        data.set(parameter, value);
    }

    private void handleLoadError(Exception e) throws InvalidConfigurationException {
        if (e instanceof YAMLException) {
            throw new InvalidConfigurationException("YAML syntax error", e);
        }
        logger.error("Configuration load error", e);
        throw new InvalidConfigurationException("Failed to load configuration", e);
    }

    public static class RobinSection {
        private Map<String, Object> sectionData;

        // Traversal helper class
        private static class TraversalResult {
            final Map<String, Object> parentMap;
            final String lastKey;

            TraversalResult(Map<String, Object> parentMap, String lastKey) {
                this.parentMap = parentMap;
                this.lastKey = lastKey;
            }
        }

        public RobinSection(Map<String, Object> sectionData) {
            this.sectionData = sectionData != null ? sectionData : new HashMap<>();
        }

        // Core traversal logic
        private TraversalResult traversePath(String parameter) {
            if (parameter == null || parameter.isEmpty()) return null;

            String[] parts = parameter.split("\\.");
            Map<String, Object> currentMap = sectionData;

            for (int i = 0; i < parts.length - 1; i++) {
                Object next = currentMap.get(parts[i]);
                if (!(next instanceof Map)) return null;
                currentMap = (Map<String, Object>) next;
            }

            return new TraversalResult(currentMap, parts[parts.length - 1]);
        }

        /**
         * Gets a string from the specified location.
         *
         * @param parameter the path to the string.
         * @return Value if parameter exists
         */
        public String getString(String parameter) {
            TraversalResult result = traversePath(parameter);
            return result != null ? (String) result.parentMap.get(result.lastKey) : null;
        }

        /**
         * Gets an Integer from the specified location.
         *
         * @param parameter the path to the Integer.
         * @return Value if parameter exists
         */
        public Integer getInt(String parameter) {
            TraversalResult result = traversePath(parameter);
            return result != null ? (Integer) result.parentMap.get(result.lastKey) : null;
        }

        /**
         * Gets a Float from the specified location.
         *
         * @param parameter the path to the Float.
         * @return Value if parameter exists
         */
        public Float getFloat(String parameter) {
            TraversalResult result = traversePath(parameter);
            if (result == null) return null;

            Object value = result.parentMap.get(result.lastKey);
            try {
                return value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        /**
         * Gets a List from the specified location.
         *
         * @param parameter the path to the List.
         * @return Value if parameter exists
         */
        @SuppressWarnings("unchecked")
        public List<Object> getList(String parameter) {
            TraversalResult result = traversePath(parameter);
            return result != null ? (List<Object>) result.parentMap.get(result.lastKey) : null;
        }

        /**
         * Gets a section from the specified path
         *
         * @param sectionName the path to the parameter.
         * @return a new RobinSection
         * @throws InvalidConfigurationException thrown if the field is not a section.
         */
        @SuppressWarnings("unchecked")
        public RobinSection getSection(String sectionName) throws InvalidConfigurationException {
            String[] parts = sectionName.split("\\.");
            Map<String, Object> currentMap = sectionData;

            for (String part : parts) {
                Object next = currentMap.get(part);
                if (!(next instanceof Map)) {
                    throw new InvalidConfigurationException("Invalid section path: " + sectionName);
                }
                currentMap = (Map<String, Object>) next;
            }
            return new RobinSection(currentMap);
        }

        // Setter with automatic map creation
        public void set(String parameter, Object value) {
            String[] parts = parameter.split("\\.");
            Map<String, Object> currentMap = sectionData;

            for (int i = 0; i < parts.length - 1; i++) {
                currentMap = (Map<String, Object>) currentMap.computeIfAbsent(
                        parts[i],
                        k -> new HashMap<>()
                );
            }

            currentMap.put(parts[parts.length - 1], value);
        }

        public Map<String, Object> getSectionData() {
            return sectionData;
        }
    }
}
