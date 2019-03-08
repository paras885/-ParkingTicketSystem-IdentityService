package org.fsociety.identityservice.exception;

/**
 * This exception is generic checked exception for business logic which enforce activities to catch these exceptions
 * and then react on basis on it. Mostly retry that same action.
 */
public class BusinessLogicRetryableException extends Exception {

    public BusinessLogicRetryableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BusinessLogicRetryableException(final String message) {
        super(message);
    }
}
