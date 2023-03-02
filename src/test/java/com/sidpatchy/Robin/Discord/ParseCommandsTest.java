package com.sidpatchy.Robin.Discord;

import com.sidpatchy.Robin.Exception.InvalidConfigurationException;
import com.sidpatchy.Robin.File.RobinConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ParseCommandsTest {

    RobinConfiguration config;
    ParseCommands parseCommands;
    private static final String testFileName = "src/test/resources/test_commands.yml";
    private static final String testURL = "https://raw.githubusercontent.com/Sidpatchy/Robin/main/src/test/resources/test_commands.yml";

    @BeforeEach
    public void setup() {
        config = new RobinConfiguration(testFileName);
        parseCommands = null;
    }

    @Test
    void getCommandName() {
        parseCommands = new ParseCommands(testFileName);
        assertEquals("a-command", parseCommands.getCommandName("a-command"));
    }

    @Test
    void getCommandUsage() {
        parseCommands = new ParseCommands(testFileName);
        assertEquals("/a-command", parseCommands.getCommandUsage("a-command"));
    }

    @Test
    void getCommandHelp() {
        parseCommands = new ParseCommands(testFileName);
        assertEquals("A command that does things.", parseCommands.getCommandHelp("a-command"));
    }

    @Test
    void getCommandOverview() {
        parseCommands = new ParseCommands(testFileName);
        assertEquals("B command is extremely useful!", parseCommands.getCommandOverview("b-command"));
    }

    @Test
    void getCommandOverviewNoOverview() {
        parseCommands = new ParseCommands(testFileName);
        assertEquals("A command that does things.", parseCommands.getCommandOverview("a-command"));
    }

    @Test
    void get() {
    }

    @Test
    void testLoadFromURL() throws IOException, InvalidConfigurationException {
        config.loadFromURL(testURL);
        parseCommands = new ParseCommands(config.getRobinSection());
        assertEquals("A command that does things.", parseCommands.getCommandHelp("a-command"));
    }

    @Test
    void testLoadSectionFromURL() throws IOException, InvalidConfigurationException {
        config.loadFromURL(testURL);
        parseCommands = new ParseCommands(config.getSection("commands"));
        assertEquals("B nested command is extremely useful!", parseCommands.getCommandOverview("b-nested-command"));
    }
}