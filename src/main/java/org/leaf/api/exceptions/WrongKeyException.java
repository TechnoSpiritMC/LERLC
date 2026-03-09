package org.leaf.api.exceptions;

public class WrongKeyException extends RuntimeException {
    public WrongKeyException(String message) {
        super(message);
    }
}
