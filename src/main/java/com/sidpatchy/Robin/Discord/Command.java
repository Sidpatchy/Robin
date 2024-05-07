package com.sidpatchy.Robin.Discord;

public class Command {
    private String name;
    private String usage;
    private String help;
    private String overview;

    /**
     * Returns the name associated with this Command.
     *
     * @return the name of the Command.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the command.
     *
     * @param name the name of the command
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the usage of the command.
     *
     * @return the usage of the command
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the usage for the command.
     *
     * @param usage the usage string
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Retrieves the help information for a command.
     *
     * @return The help information for the command.
     */
    public String getHelp() {
        return help;
    }

    /**
     * Sets the help text for the command.
     *
     * @param help The help text to set.
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Retrieves the overview of the command.
     *
     * @return the overview of the command as a String
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Sets the overview for the command.
     *
     * @param overview the overview to be set for the command
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }
}
