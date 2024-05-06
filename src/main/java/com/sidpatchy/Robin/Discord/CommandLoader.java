package com.sidpatchy.Robin.Discord;

import com.sidpatchy.Robin.File.RobinConfiguration;
import com.sidpatchy.Robin.File.RobinConfiguration.RobinSection;
import com.sidpatchy.Robin.Exception.InvalidConfigurationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandLoader {
    public static Map<String, Command> loadCommands(String filePath) throws InvalidConfigurationException, IOException {
        RobinConfiguration config = new RobinConfiguration(filePath);
        config.load();  // Load the YAML file

        Map<String, Command> commands = new HashMap<>();
        RobinSection commandsSection = config.getSection("commands");

        if (commandsSection != null) {
            Map<String, Object> commandsData = commandsSection.getSectionData();
            for (Map.Entry<String, Object> entry : commandsData.entrySet()) {
                String commandName = entry.getKey();
                Map<String, Object> properties = (Map<String, Object>) entry.getValue();
                Command command = new Command();
                command.setName((String) properties.get("name"));
                command.setUsage((String) properties.get("usage"));
                command.setHelp((String) properties.get("help"));
                command.setOverview((String) properties.get("overview"));
                commands.put(commandName, command);
            }
        }
        return commands;
    }
}
