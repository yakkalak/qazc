package com.kiran.user.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserNotAuthenticatedException extends Exception{

    private static final long serialVersionUID = 1L;

    public UserNotAuthenticatedException(String message){
        super(message);
    }
}
