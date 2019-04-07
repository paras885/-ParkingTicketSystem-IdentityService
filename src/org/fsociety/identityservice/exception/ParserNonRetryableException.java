package org.fsociety.identityservice.exception;

public class ParserNonRetryableException extends Exception {

    public ParserNonRetryableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ParserNonRetryableException(final String message) {
        super(message);
    }
}
