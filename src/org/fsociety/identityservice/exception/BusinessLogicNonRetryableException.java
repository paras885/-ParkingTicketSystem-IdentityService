package org.fsociety.identityservice.exception;

/**
 * This exception is generic checked exception for business logic which enforce activities to catch these exceptions
 * and then react on basis on it.
 */
public class BusinessLogicNonRetryableException extends Exception {

    public BusinessLogicNonRetryableException(final String exceptionMessage, final Throwable exceptionCause) {
        super(exceptionMessage, exceptionCause);
    }
}
