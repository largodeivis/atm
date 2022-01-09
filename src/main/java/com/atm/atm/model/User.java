package com.atm.atm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
public class User {
    @GeneratedValue
    long id;

    @NotBlank
    @Setter
    String userId;

    @Size(min=4, message="PIN must be 4 or more digits.")
    @PositiveOrZero(message="PIN must be numerical.")
    @Setter
    String pin;

    private Account account;

    protected User(){
    }

    public User(String userId, String pin, Account account) {
        this.userId = userId;
        this.pin = pin;
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", balance =" + account.getBalance() +
                ", pin='" + pin + '\'' +
                '}';
    }
}
