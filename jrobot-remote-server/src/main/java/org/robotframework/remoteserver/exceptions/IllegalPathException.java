package org.robotframework.remoteserver.exceptions;

/**
 * Thrown when a path is not valid for mapping to a library
 */
public class IllegalPathException extends RuntimeException {

    /**
     * @param message Description of error
     */
    public IllegalPathException(String message) {
        super(message);
    }
}
