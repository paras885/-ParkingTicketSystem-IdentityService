package org.fsociety.identityservice.exception;

/**
 * This exception is generic checked exception for DAOs which enforce businessLogics to catch these exceptions
 * and then react on basis on it. Mostly retry that same action.
 */
public class DAORetryableException extends Exception {

    public DAORetryableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
