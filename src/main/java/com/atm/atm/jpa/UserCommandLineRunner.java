package com.atm.atm.jpa;

import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserCommandLineRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UserCommandLineRunner.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        Account account1 = accountRepository.save(new Account(new BigDecimal("100.00")));
        userRepository.save(new User(123456789, "Shaggy", "1234", account1));

        Account account2 = accountRepository.save(new Account(new BigDecimal("5050.58")));
        userRepository.save(new User(987654321,"Scooby", "1111", account2));

        for (User user : userRepository.findAll()){
            log.info(user.toString());
        }

    }
}
