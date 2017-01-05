package ru.levelp.api.exceptions;

/**
 * Created by кайрат on 03.01.2017.
 */
public class TokenException extends Exception {
    public TokenException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
