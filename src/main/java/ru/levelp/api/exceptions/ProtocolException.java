package ru.levelp.api.exceptions;

/**
 * Created by кайрат on 02.01.2017.
 */
public class ProtocolException extends Exception {

    public ProtocolException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
