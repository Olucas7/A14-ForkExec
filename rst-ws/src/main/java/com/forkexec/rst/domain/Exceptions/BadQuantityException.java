package com.forkexec.rst.domain.Exceptions;

public class BadQuantityException extends Exception {
    private static final long serialVersionUID = 1L;
    private String _message;


    public BadQuantityException(String message) {
        this._message = message;
    }


    public String getMessage() {
        return this._message;
    }

}