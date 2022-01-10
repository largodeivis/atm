package com.atm.atm.service;

import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.jpa.AccountRepository;
import com.atm.atm.jpa.UserRepository;
import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    public Iterable<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> retrieveUser(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<Account> retrieveAccount(long userId, String pin) throws InvalidCredentialsException {
        if (verifyCredentials(userId, pin)) {
            Optional<User> user = userRepository.findById(userId);

            if (user.isEmpty()) {
                throw new InvalidCredentialsException(userId);
            }
            return Optional.of(user.get().getAccount());
        }
        throw new InvalidCredentialsException(userId);
    }

    public User createUser(User user) {
        try {
            accountRepository.save(user.getAccount());
        } catch (Exception ex){
            if(user.getAccount().getBalance().compareTo(BigDecimal.ZERO) < 0){
                logger.error("Starting balance should be 0 or more.");
            }
            return null;
        }
        try {
            return userRepository.save(user);
        } catch (Exception ex){
            if (user.getPin().length() < 4 || user.getPin().matches("^\\d+$")){
                logger.error("Pin should be greater than 4 digits and must be numeric");
            } else if (user.getUserId().isBlank()){
                logger.error("User ID can't be empty");
            }
            return null;
        }
    }

    public Boolean verifyCredentials(Long userId, String pin) throws InvalidCredentialsException {
        boolean valid = false;
        Optional<User> user = retrieveUser(userId);
        if (user.isEmpty()){
            throw new InvalidCredentialsException(userId);
        }
        if (user.get().getPin().equals(pin)){
            valid = true;
        }
        return valid;
    }
}
