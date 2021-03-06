package com.atm.atm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @PositiveOrZero(message="Balance should be greater than or equal to 0.")
    private BigDecimal balance;

    protected Account(){
    };

    public Account(BigDecimal balance) {
        this.balance = setBalanceScale(balance);
    }

    public Account(String balance) {
        this.balance = setBalanceScale(balance);
    }

    public Account(long id, BigDecimal balance) {
        this.id = id;
        this.balance = setBalanceScale(balance);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = setBalanceScale(balance);
    }

    private BigDecimal setBalanceScale(BigDecimal balance){
        return balance.setScale(2);
    }

    private BigDecimal setBalanceScale(String balance){
        BigDecimal newBalance = new BigDecimal(balance);
        return newBalance.setScale(2);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
