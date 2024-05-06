package com.sidpatchy.Robin.Discord;


import com.sidpatchy.Robin.Exception.InvalidConfigurationException;
import com.sidpatchy.Robin.File.RobinConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * A class to make command files easier to work with.
 *
 * @author Sidpatchy
 */
@Deprecated
public class ParseCommands {

    private final Logger logger = LogManager.getLogger();
    private final String commandsFile;
    static RobinConfiguration config;
    RobinConfiguration.RobinSection section;

    /**
     * Gets commands from the root of the commands file.
     *
     * @param commandsFile path to the commands file
     */
    @Deprecated
    public ParseCommands(String commandsFile) {
        this.commandsFile = commandsFile;
        config = new RobinConfiguration(commandsFile);
        try {
            config.load();
            section = config.getRobinSection();
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets commands from a section from the commands file.
     *
     * @param commandsFile path to the commands file
     * @param sectionName name of the section within the commands file
     */
    @Deprecated
    public ParseCommands(String commandsFile, String sectionName) {
        this.commandsFile = commandsFile;
        config = new RobinConfiguration(commandsFile);
        try {
            config.load();
            section = config.getSection(sectionName);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Accepts a RobinSection to allow loading files from any source supported by RobinConfiguration.
     *
     * @param section the RobinSection.
     */
    @Deprecated
    public ParseCommands(RobinConfiguration.RobinSection section) {
        commandsFile = "";
        this.section = section;
    }

    @Deprecated
    private RobinConfiguration.RobinSection getCommand(String command) {
        try {
            return section.getSection(command);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to read " + command + " from commands file");
            return null;
        }
    }

    /**
     * Gets a command's name value.
     *
     * @param command
     * @return
     */
    @Deprecated
    public String getCommandName(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).getString("name");
    }

    @Deprecated
    public String getCommandUsage(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).getString("usage");
    }

    @Deprecated
    public String getCommandHelp(String command) {
        return Objects.requireNonNull(getCommand(command)).getString("help");
    }

    @Deprecated
    public String getCommandOverview(String command) {
        String overview = (String) Objects.requireNonNull(getCommand(command)).getString("overview");
        if (overview.equalsIgnoreCase("")) {
            overview = getCommandHelp(command);
        }
        return overview;
    }

    @Deprecated
    public String getCustomField(String command, String field) {
        return Objects.requireNonNull(getCommand(command)).getString(field);
    }

    @Deprecated
    public HashMap<String, String> get(String command) {
        return new HashMap<String, String>() {{
            put("name", getCommandName(command));
            put("usage", getCommandUsage(command));
            put("help", getCommandHelp(command));
            put("overview", getCommandOverview(command));
        }};
    }
}
