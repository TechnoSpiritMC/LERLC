package org.leaf.api.exceptions;

public class InvalidatedKeyException extends RuntimeException {
    public InvalidatedKeyException(String message) {
        super(message);
    }
}
