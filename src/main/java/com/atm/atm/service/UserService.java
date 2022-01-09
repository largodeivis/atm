package com.atm.atm.service;

import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static List<User> users = new ArrayList<>();
    static {
        Account account1 = new Account(new BigDecimal("100.00"));
        User user1 = new User("user1", "1234", account1);

        Account account2 = new Account(new BigDecimal("5050.58"));
        User user2 = new User("user2", "1111", account2);

        Account account3 = new Account(new BigDecimal("7.12"));
        User user3 = new User("user3", "5555", account3);

        users.add(user1);
        users.add(user2);
        users.add(user3);
    }

    public List<User> retrieveAllUsers(){
        return users;
    }

    public User retrieveUser(long userId){
        for(User user : users){
            if(user.getId() == userId){
                return user;
            }
        }
        return null;
    }

    public Account retrieveAccount(long userId){
        for(User user : users){
            if(user.getId() == userId){
                return user.getAccount();
            }
        }
        return null;
    }

    public User createUser(String userId, String pin, String balance){
        Account account = new Account(new BigDecimal(balance));
//        long randomId = new Random().nextLong();
//        account.setId(Math.abs(randomId));
        User user = new User(userId, pin, account);
        users.add(user);
        return user;
    }

    public User createUser(User user){
//        long randomId = new Random().nextLong();
//        user.setId(Math.abs(randomId));
        users.add(user);
        return user;
    }



}
