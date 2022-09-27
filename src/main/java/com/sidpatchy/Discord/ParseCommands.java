package com.sidpatchy.Discord;


import com.sidpatchy.File.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Parse nested bits of the commands file.
 *
 * @author Sidpatchy
 */
public class ParseCommands {

    private static final Logger logger = LogManager.getLogger();
    static ConfigReader config = new ConfigReader();
    private static String commandsFile;

    public ParseCommands(String commandsFile) {
        ParseCommands.commandsFile = commandsFile;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getCommand(String command) {
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

    public static String getCommandName(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).get("name");
    }

    public static String getCommandUsage(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).get("usage");
    }

    public static String getCommandHelp(String command) {
        return (String) Objects.requireNonNull(getCommand(command)).get("help");
    }

    public static HashMap<String, String> get(String command) {
        return new HashMap<String, String>() {{
            put("name", getCommandName(command));
            put("usage", getCommandUsage(command));
            put("help", getCommandHelp(command));
        }};
    }
}
