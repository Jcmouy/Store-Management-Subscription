package com.coffee.shop.exception;

public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException() {
        super("The item is already in the database!");
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
