package com.coffee.shop.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException() {
        super("Could not found item in database!");
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
