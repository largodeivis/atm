package com.atm.atm.exception;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(Long userId){
        super("Invalid Credentials provided for user: " + userId.toString() + ".");
    }
}
