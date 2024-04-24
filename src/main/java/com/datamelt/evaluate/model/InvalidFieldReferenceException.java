package com.datamelt.evaluate.model;

public class InvalidFieldReferenceException extends RuntimeException
{
    public InvalidFieldReferenceException(String errorMessage) {
        super(errorMessage);
    }
}
