package com.coffee.shop.exception;

public class NotValidException extends RuntimeException{

    public NotValidException() {
        super("The request is not valid!");
    }

    public NotValidException(String message) {
        super(message);
    }
}
