package com.wl.counter.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Cannot find user with provided login.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
