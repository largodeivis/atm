package com.atm.atm.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException(String message){
        super(message);
    }
    public InsufficientBalanceException(BigDecimal balance, BigDecimal amount){
        super("There are insufficient funds to withdraw the desired amount. " +
                "Current Balance: $" + balance.toString() + " Withdraw Amount: $" + amount.toString());
    }

    public InsufficientBalanceException(BigDecimal balance, String amount){
        super("There are insufficient funds to withdraw the desired amount. " +
                "Current Balance: $" + balance.toString() + " Withdraw Amount: $" + amount);
    }
}
