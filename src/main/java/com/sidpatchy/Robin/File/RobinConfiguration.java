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
import java.util.List;
import java.util.Map;

public class RobinConfiguration {
    private static RobinSection data;
    private String fileName;

    private Yaml yaml = new Yaml();
    private static final Logger logger = LogManager.getLogger();

    /**
     * Construct a new RobinConfiguration with a file
     * @param fileName the file on the hard drive.
     */
    public RobinConfiguration(String fileName) {
        this.fileName = fileName;
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
        if (fileName == null) {
            throw new InvalidConfigurationException("A file must be specified");
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            data = new RobinSection(yaml.load(fileInputStream));
        }
        catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        }
        catch (IOException e) {
            logger.error(e);
            throw new InvalidConfigurationException("I/O error encountered while loading file: " + fileName, e);
        }
    }

    /**
     * Load a YAML from a string.
     *
     * @param contents a valid YAML string.
     * @throws InvalidConfigurationException
     */
    public void loadFromString(String contents) throws InvalidConfigurationException {
        if (contents == null) {
            throw new InvalidConfigurationException("Contents cannot be null");
        }

        try {
            data = new RobinSection(yaml.load(contents));
        }
        catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        }
    }

    /**
     * Gets a YAML file from a URL.
     *
     * @param link the link to read from.
     * @throws IOException thrown if unable to access the provided link.
     * @throws InvalidConfigurationException thrown if the YAML file is invalid.
     */
    public void loadFromURL(String link) throws IOException, InvalidConfigurationException {
        URL url;
        InputStreamReader reader = null;
        try {
            url = new URL(link);
            reader = new InputStreamReader(url.openStream());

            data = new RobinSection(yaml.load(reader));
        } catch (IOException e) {
            logger.error(e);
            logger.error("Unable to read from " + link);
            throw new IOException("Failed GET from " + link);
        }
        catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
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
        URL url;
        InputStreamReader reader;
        try {
            url = new URL(link);
            URLConnection uc = url.openConnection();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            reader = new InputStreamReader(uc.getInputStream());

            data = new RobinSection(yaml.load(reader));
        }
        catch (IOException e) {
            logger.error(e);
            logger.error("Unable to read from " + link);
            throw new IOException("Failed GET from " + link);
        }
        catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        }
    }

    public void saveToFile() throws IOException, InvalidConfigurationException {
        if (fileName == null) {
            throw new InvalidConfigurationException("A file must be specified");
        }

        // Create DumperOptions with the required settings
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        FileWriter writer = new FileWriter(fileName);
        Yaml yaml = new Yaml(dumperOptions);
        yaml.dump(data.getSectionData(), writer);
        writer.close();
    }

    public String saveToString() {
        StringWriter writer = new StringWriter();
        yaml.dump(data.sectionData, writer);
        return writer.toString();
    }

    /**
     * Calls RobinSection.getString() on the root of the YAML file.
     *
     * @param parameter parameter to get from the YAML file.
     * @return value of parameter if present.
     */
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

    /**
     * Returns a RobinSection for the root of the YAML file.
     * @return
     */
    public RobinSection getRobinSection() {
        return data;
    }

    public static class RobinSection {
        private final Map<String, Object> sectionData;

        /**
         * Constructs a new RobinSection
         * @param sectionData Map of YAML file / parent Section
         */
        public RobinSection(Map<String, Object> sectionData) {
            this.sectionData = sectionData;
        }

        /**
         * Gets a string from the specified location.
         *
         * @param parameter the path to the string.
         * @return Value if parameter exists
         */
        public String getString(String parameter) {
            return (String) sectionData.get(parameter);
        }

        /**
         * Gets an Integer from the specified location.
         *
         * @param parameter the path to the Integer.
         * @return Value if parameter exists
         */
        public Integer getInt(String parameter) {
            return (Integer) sectionData.get(parameter);
        }

        /**
         * Gets a Float from the specified location.
         *
         * @param parameter the path to the Float.
         * @return Value if parameter exists
         */
        public Float getFloat(String parameter) {
            return Float.valueOf(sectionData.get(parameter).toString());
        }

        /**
         * Gets a long from the specified location.
         *
         * @param parameter the path to the long.
         * @return Value if parameter exists
         */
        public long getLong(String parameter) {
            return (long) sectionData.get(parameter);
        }

        /**
         * Gets a List from the specified location.
         *
         * @param parameter the path to the List.
         * @return Value if parameter exists
         */
        @SuppressWarnings("unchecked")
        public List<Object> getList(String parameter) {
            return (List<Object>) sectionData.get(parameter);
        }

        /**
         * Gets an Object from the specified location.
         *
         * @param parameter the path to the Object.
         * @return Value if parameter exists
         */
        public Object getObj(String parameter) {
            return sectionData.get(parameter);
        }

        /**
         * Sets the value of the specified parameter. Overwrites any existing values.
         * Running this will discard all comments in the file!
         *
         * @param parameter the path to the object you'd like to update.
         * @param value new value
         */
        public void set(String parameter, Object value) {
            sectionData.put(parameter, value);
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
            try {
                Map<String, Object> sectionData2 = (Map<String, Object>) sectionData.get(sectionName);
                return new RobinSection(sectionData2);
            } catch (ClassCastException e) {
                throw new InvalidConfigurationException(e);
            }
        }

        /**
         * Converts the RobinSection back into a Map for a serializer to use.
         * <p>
         * You probably don't need to use this.
         *
         * @return map for serializer.
         */
        public Map<String, Object> getSectionData() {
            return sectionData;
        }
    }
}