package com.atm.atm.exception;

public class InsufficientStartingBalanceException extends Exception{
    public InsufficientStartingBalanceException(){
        super("Starting balance should be 0 or more.");
    }
}
