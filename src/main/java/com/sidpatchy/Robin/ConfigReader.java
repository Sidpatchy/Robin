package com.sidpatchy.Robin.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ConfigReader {

    Yaml yaml = new Yaml();
    private static final Logger logger = LogManager.getLogger();

    public Map<String, Object> GetConfig(String file) {
        try {
            InputStream inputStream = new FileInputStream(new File("config/" + file));
            return yaml.load(inputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to read from file " + file + ". More errors will follow.");
            return null;
        }
    }

    public boolean getBool(String file, String parameter) {
        Map<String, Object> config = GetConfig(file);
        return (boolean) config.get(parameter);
    }

    public String getString(String file, String parameter) {
        Map<String, Object> config = GetConfig(file);
        return (String) config.get(parameter);
    }

    public Integer getInt(String file, String parameter) {
        Map<String, Object> config = GetConfig(file);
        return (Integer) config.get(parameter);
    }

    public Float getFloat(String file, String parameter) {
        Map<String, Object> config = GetConfig(file);
        return (Float) config.get(parameter);
    }

    public long getLong(String file, String parameter) {
        Map<String, Object> config = GetConfig(file);
        return (long) config.get(parameter);
    }

    @SuppressWarnings("unchecked")
    public List<String> getList(String file, String parameter) {
        return (List<String>) GetConfig(file).get(parameter);
    }

    public Object getObj(String file, String parameter) {
        Map<String, Object> config = GetConfig(file);
        return config.get(parameter);
    }
}
