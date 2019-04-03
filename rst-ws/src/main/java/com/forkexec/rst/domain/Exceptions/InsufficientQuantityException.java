package com.forkexec.rst.domain.Exceptions;

public class InsufficientQuantityException extends Exception {
    private static final long serialVersionUID = 1L;
    private String _message;

    public InsufficientQuantityException(String message) {
        this._message = message;
    }


    public String getMessage() {
        return this._message;
    }

}