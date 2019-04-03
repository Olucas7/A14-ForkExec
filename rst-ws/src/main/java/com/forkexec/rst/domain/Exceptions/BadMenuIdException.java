package com.forkexec.rst.domain.Exceptions;

public class BadMenuIdException extends Exception {
    private static final long serialVersionUID = 1L;
    private String _id;


    public BadMenuIdException(String id) {
        this._id = id;
    }
    

    public String getId() {
        return this._id;
    }

}