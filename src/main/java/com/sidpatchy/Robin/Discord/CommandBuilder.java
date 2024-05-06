package com.sidpatchy.Robin.Discord;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * The CommandBuilder class is responsible for building a command object.
 * It provides methods to set the properties of the command object and load command data from different sources.
 */
public class CommandBuilder {
    private Command command;

    /**
     * The CommandBuilder class is responsible for building a command object.
     * It provides methods to set the properties of the command object and load command data from different sources.
     */
    public CommandBuilder() {
        command = new Command();
    }

    /**
     * The CommandBuilder class is responsible for building a command object.
     * It provides methods to set the properties of the command object and load command data from different sources.
     * <p>
     * This signature allows for modification of an existing Command object.
     */
    public CommandBuilder(Command command) {
        this.command = command;
    }

    /**
     * Sets the name of the command.
     *
     * @param name the name of the command
     * @return the CommandBuilder instance
     */
    public CommandBuilder setName(String name) {
        command.setName(name);
        return this;
    }

    /**
     * Sets the usage for the command.
     *
     * @param usage the usage string
     * @return the CommandBuilder object
     */
    public CommandBuilder setUsage(String usage) {
        command.setUsage(usage);
        return this;
    }

    /**
     * Sets the help text for the command.
     *
     * @param help The help text to set.
     * @return The CommandBuilder instance with the help text set.
     */
    public CommandBuilder setHelp(String help) {
        command.setHelp(help);
        return this;
    }

    /**
     * Sets the overview for the command.
     *
     * @param overview the overview to be set for the command
     * @return the CommandBuilder object
     */
    public CommandBuilder setOverview(String overview) {
        command.setOverview(overview);
        return this;
    }

    /**
     * Loads a YAML file containing command data and constructs a CommandBuilder object.
     *
     * @param filePath The path of the YAML file to load.
     * @return A CommandBuilder object constructed from the loaded YAML file.
     * @throws FileNotFoundException If the specified file cannot be found.
     */
    public CommandBuilder loadFromFile(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        return loadFromInputStream(inputStream);
    }

    /**
     * Loads a YAML file from the specified URL and populates a CommandBuilder object with the contents.
     *
     * @param urlString the URL of the YAML file to load
     * @return a CommandBuilder object populated with the contents of the YAML file
     * @throws IOException if there is an error opening the URL or reading from the stream
     */
    public CommandBuilder loadFromURL(String urlString) throws IOException {
        InputStream inputStream = new URL(urlString).openStream();
        return loadFromInputStream(inputStream);
    }

    /**
     * Loads the content from an input stream and returns a CommandBuilder instance.
     *
     * @param inputStream The input stream to read the content from.
     *
     * @return A CommandBuilder instance with the content loaded from the input stream.
     */
    private CommandBuilder loadFromInputStream(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
        String yamlContent = scanner.hasNext() ? scanner.next() : "";
        return loadFromString(yamlContent);
    }

    /**
     * Loads the command from the given YAML string.
     *
     * @param yamlString the YAML string representing the command
     * @return the command builder instance
     */
    public CommandBuilder loadFromString(String yamlString) {
        Yaml yaml = new Yaml();
        command = yaml.loadAs(yamlString, Command.class);
        return this;
    }

    /**
     * Builds and returns the command object.
     *
     * @return The built command object.
     */
    public Command build() {
        return command;
    }
}
