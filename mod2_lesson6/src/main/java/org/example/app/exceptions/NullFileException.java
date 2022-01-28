package org.example.app.exceptions;

public class NullFileException extends Exception {

    private final String message;

    public NullFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
