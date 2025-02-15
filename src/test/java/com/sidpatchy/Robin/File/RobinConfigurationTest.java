package com.sidpatchy.Robin.File;

import com.sidpatchy.Robin.Exception.InvalidConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobinConfigurationTest {

    private static final String testFileName = "src/test/resources/test_config.yml";
    private static final String testURL = "https://raw.githubusercontent.com/Sidpatchy/Robin/main/src/test/resources/test_config.yml";
    private RobinConfiguration config;

    @BeforeEach
    public void setup() {
        config = new RobinConfiguration(testFileName);
    }

    @Test
    void load() throws InvalidConfigurationException {
        config.load();
    }

    @Test
    void loadFromString() throws InvalidConfigurationException {
        config.loadFromString("test: success");
        assertEquals("success", config.getString("test"));
    }

    @Test
    void loadFromURL() throws InvalidConfigurationException, IOException {
        config.loadFromURL(testURL);
        assertEquals("ey", config.getSection("a-double-nested-field").getSection("a").getString("pronunciation"));
    }

    @Test
    void loadFromInvalidURL() throws IOException, InvalidConfigurationException {
        try {
            config.loadFromURL("https://invalid.poopfeast.net/");
        }
        catch (IOException e) {
            // Expected error
        }
    }

    @Test
    void loadFromURLBasicAuth() {
        // NYI
    }

    @Test
    void loadFromInvalidURLBasicAuth() throws IOException, InvalidConfigurationException {
        try {
            config.loadFromURL("username", "password", "https://invalid.poopfeast.net/");
        }
        catch (IOException e) {
            // Expected error
        }
    }

    @Test
    void saveToString() throws InvalidConfigurationException {
        config.loadFromString("the: cat");
        assertEquals("{the: cat}\n", config.saveToString());
    }

    @Test
    void getString() throws InvalidConfigurationException {
        config.loadFromString("test: great success!");
        assertEquals("great success!", config.getString("test"));
    }

    @Test
    void getInt() throws InvalidConfigurationException {
        config.loadFromString("test: 123");
        assertEquals(123, config.getInt("test"));
    }

    @Test
    void getFloat() throws InvalidConfigurationException {
        config.loadFromString("test: 1.23");
        assertEquals(Float.valueOf(1.23f), config.getFloat("test"));
    }

    @Test
    void getLong() throws InvalidConfigurationException {
        config.loadFromString("test: 100000000000000000");
        assertEquals(100000000000000000L, config.getLong("test"));
    }

    @Test
    void getList() throws InvalidConfigurationException {
        config.loadFromString("test:\n  - item1\n  - item2\n  - item3");
        List<String> list = config.getList("test", String.class);
        assertEquals(3, list.size());
        assertEquals("item1", list.get(0));
        assertEquals("item2", list.get(1));
        assertEquals("item3", list.get(2));
    }

    @Test
    void getObj() throws InvalidConfigurationException {
        config.loadFromString("test: value");
        assertEquals("value", config.getObj("test"));
    }

    @Test
    void getSection() throws InvalidConfigurationException {
        config.loadFromString("test:\n  key: value");
        RobinConfiguration.RobinSection section = config.getSection("test");
        assertNotNull(section);
        assertEquals("value", section.getString("key"));
    }

    @Test
    void set() {
    }

    @Test
    void getRobinSection() throws InvalidConfigurationException {
        config.load();
        config.getSection("a-nested-field").set("c", "the third letter");
        assertEquals("the third letter", config.getSection("a-nested-field").getString("c"));
    }
}