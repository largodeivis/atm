package com.atm.atm.exception;

import java.math.BigDecimal;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(Long userId){
        super("Invalid Credentials provided for user: " + userId.toString() + ".");
    }
}
