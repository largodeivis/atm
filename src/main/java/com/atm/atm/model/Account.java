package com.atm.atm.model;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class Account {
    @GeneratedValue
    @Id
    private long id;

    @PositiveOrZero(message="Balance should be greater than or equal to 0.")
    private BigDecimal balance;

    protected Account(){
    };

    public Account(BigDecimal balance) {
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
