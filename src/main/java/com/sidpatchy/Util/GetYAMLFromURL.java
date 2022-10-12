package com.sidpatchy.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class GetYAMLFromURL {

    Yaml yaml = new Yaml();
    private static final Logger logger = LogManager.getLogger();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getYAMLFromURL(String link) {
        URL url = null;
        InputStreamReader reader = null;
        try {
            url = new URL(link);
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            logger.error(e.getStackTrace());
            logger.error("Unable to read from " + link);
        }

        return yaml.load(reader);
    }
}