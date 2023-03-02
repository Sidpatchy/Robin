package com.sidpatchy.Robin.Exception;

public class InvalidConfigurationException extends Exception {

    /**
     * Create exception without message or cause.
     */
    public InvalidConfigurationException() {}

    /**
     * Create exception with message
     * @param errorMessage details
     */
    public InvalidConfigurationException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Create exception with cause.
     * @param cause cause of exception
     */
    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Create exception with message and cause.
     * @param errorMessage details
     * @param cause cause of exception
     */
    public InvalidConfigurationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
