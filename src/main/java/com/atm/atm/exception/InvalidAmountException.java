package com.atm.atm.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends Exception {
    public InvalidAmountException(Long userId, BigDecimal amount){
        super("Invalid Amount provided for user.\nUser: " + userId + "\nAmount:" + amount.toString());
    }
}
