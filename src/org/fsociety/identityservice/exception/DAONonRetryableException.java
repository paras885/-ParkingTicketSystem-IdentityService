package org.fsociety.identityservice.exception;

/**
 * This exception is generic checked exception for DAOs which enforce businessLogics to catch these exceptions
 * and then react on basis on it.
 */
public class DAONonRetryableException extends Exception {

    public DAONonRetryableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DAONonRetryableException(final String message) {
        super(message);
    }
}
