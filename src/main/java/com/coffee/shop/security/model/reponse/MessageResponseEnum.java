package com.coffee.shop.security.model.reponse;

public enum MessageResponseEnum {
    USERNAME_TAKEN("Error: Username is already taken!"),
    EMAIL_TAKEN("Error: email is already taken!"),
    USER_REGISTERED_SUCCESSFULLY("User registered successfully!");

    private String message;

    public String getMessageResponseEnum()
    {
        return this.message;
    }

    private MessageResponseEnum(String message)
    {
        this.message = message;
    }

}
