package com.sidpatchy.Robin.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Map;

public class GetYAMLFromURL {

    Yaml yaml = new Yaml();
    private static final Logger logger = LogManager.getLogger();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getYAMLFromURL(String link) throws IOException {
        URL url = null;
        InputStreamReader reader = null;
        try {
            url = new URL(link);
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            logger.error(e.getStackTrace());
            logger.error("Unable to read from " + link);
            throw new IOException("Failed GET from " + link);
        }

        return yaml.load(reader);
    }

    /**
     * Gets YAML from URL when basic authentication is required
     *
     * @param username
     * @param password
     * @param link
     * @return
     */
    public Map<String, Object> getYAMLFromURL(String username, String password, String link) throws IOException{
        URL url = null;
        InputStreamReader reader = null;
        try {
            url = new URL(link);
            URLConnection uc = url.openConnection();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            reader = new InputStreamReader(uc.getInputStream());
        } catch (IOException e) {
            logger.error(e.getStackTrace());
            logger.error("Unable to read from " + link);
            throw new IOException("Failed GET from " + link);
        }

        return yaml.load(reader);
    }
}