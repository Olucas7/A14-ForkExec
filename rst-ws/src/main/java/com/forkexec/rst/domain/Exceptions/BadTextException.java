package com.forkexec.rst.domain.Exceptions;

public class BadTextException extends Exception {
    private static final long serialVersionUID = 1L;
    private String _message;

    public BadTextException(String message) {
        this._message = message;
    }
    

    public String getId() {
        return this._message;
    }

}