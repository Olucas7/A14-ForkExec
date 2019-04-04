package com.forkexec.pts.domain.Exceptions;

public class EmailAlreadyExistsException extends Exception {
    private final String MESSAGE = "Email already exists";

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}