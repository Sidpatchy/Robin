package com.sidpatchy.Robin.Discord;


import com.sidpatchy.Robin.File.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Parse nested bits of the commands file.
 *
 * @author Sidpatchy
 */
public class ParseCommands {

    private final Logger logger = LogManager.getLogger();
    static ConfigReader config = new ConfigReader();
    private final String commandsFile;

    public ParseCommands(String commandsFile) {
        this.commandsFile = commandsFile;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCommand(String command) {
        try {
            Map<String , Object> obj = (Map<String, Object>) config.getObj(commandsFile, "commands");
            return (Map<String, Object>) obj.get(command);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to read " + command + " from commands file");
            return null;
        }
    }

    public String getCommandName(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).get("name");
    }

    public String getCommandUsage(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).get("usage");
    }

    public String getCommandHelp(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).get("help");
    }

    public HashMap<String, String> get(String command) {
        return new HashMap<String, String>() {{
            put("name", getCommandName(command));
            put("usage", getCommandUsage(command));
            put("help", getCommandHelp(command));
        }};
    }
}
