package com.atm.atm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class User {

    @Id
    private long id;

    @NotBlank
    @Column(unique = true)
    private String userId;

    @Size(min=4, message="PIN must be 4 or more digits.")
    @PositiveOrZero(message="PIN must be numerical.")
    private String pin;

    @OneToOne
    private Account account;

    protected User(){
    }

    public User(String userId, String pin, Account account) {
        this.userId = userId;
        this.pin = pin;
        this.account = account;
    }

    public User(long id, String userId, String pin, Account account) {
        this.id = id;
        this.userId = userId;
        this.pin = pin;
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", pin='" + pin + '\'' +
                ", account=" + account +
                '}';
    }
}
