package com.atm.atm.service;

import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.jpa.AccountRepository;
import com.atm.atm.jpa.UserRepository;
import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    private UserService userService;

    Account account1 = new Account("1000.00");
    User user1 = new User(1234, "Freddy", "1111", account1);

    Account account2 = new Account("200.00");
    User user2 = new User(4321, "Daphne", "2222", account2);

    List<User> userList;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, accountRepository);
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
    }

    @AfterEach
    public void tearDown() {
        userService = null;
        userList = null;
        user1 = null;
        user2 = null;
        account1 = null;
        account2 = null;
    }

    @Test
    void validCredentialsReturnTrue() throws InvalidCredentialsException {
        long userId = 1234;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Boolean validCredentials = userService.verifyCredentials(userId, "1111");
        assertTrue(validCredentials);
    }

    @Test
    void invalidCredentialsReturnFalse() throws InvalidCredentialsException {
        long userId = 1234;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Boolean validCredentials = userService.verifyCredentials(userId, "5555");
        assertFalse(validCredentials);
    }

    @Test
    void validCredentialsRetrieveAccount() throws InvalidCredentialsException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        Optional<Account> userAccount = userService.retrieveAccount(1234L, "1111");
        assertTrue(userAccount.isPresent());
        assertEquals(new BigDecimal("1000.00"), userAccount.get().getBalance());
    }

    @Test
    void invalidCredentialsRetrieveAccountThrowsException() {
        long userId = 1234;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user2));
        Exception thrown = assertThrows(InvalidCredentialsException.class, () -> userService.retrieveAccount(1234, "5555"));
        assertEquals("Invalid Credentials provided for user: 1234.", thrown.getMessage());
    }


}
