package org.fsociety.identityservice.lib.parser;

import org.fsociety.identityservice.exception.ParserNonRetryableException;

@FunctionalInterface
public interface Parser<OutputType, InputType> {

    OutputType parse(final InputType input) throws ParserNonRetryableException;
}
