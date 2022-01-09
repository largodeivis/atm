//package com.atm.atm.service;
//
//import com.atm.atm.model.Account;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//public class AccountService {
//    public Account createAccount(String balance){
//        return new Account(new BigDecimal(balance));
//    }
//
//    public Account createAccount(BigDecimal balance){
//        return new Account(balance);
//    }
//
//    public BigDecimal retrieveBalance(String userid){
//        for(Account account : accounts){
//            if (account.getCustomer().equalsIgnoreCase(userid)){
//                return account.getBalance();
//            }
//        }
//
//        return new BigDecimal("0.00"); //Change this to error
//    }
//
//    public BigDecimal depositMoney(String userid, BigDecimal amount){
//        for(Account account : accounts){
//            if (account.getCustomer().equalsIgnoreCase(userid)){
//                return account.getBalance().add(amount);
//            }
//        }
//        return new BigDecimal("0.00"); //change this to error
//    }
//
//    public BigDecimal depositMoney(String userid, String amount){
//        for(Account account : accounts){
//            if (account.getCustomer().equalsIgnoreCase(userid)){
//                BigDecimal newBalance = account.getBalance().add(new BigDecimal(amount));
//                account.setBalance(newBalance);
//                return newBalance;
//            }
//        }
//        return new BigDecimal("0.00"); //change this to error
//    }
//
//    public BigDecimal withdrawMoney(String user, String amount) throws InsufficientBalanceException {
//        for(Account account : accounts){
//            if (account.getCustomer().equalsIgnoreCase(user)){
//                BigDecimal balance = account.getBalance();
//                if (balance.compareTo(BigDecimal.ZERO) > 0) {
//                    BigDecimal finalMoneyAmount = balance.subtract(new BigDecimal(amount));
//                    if (finalMoneyAmount.compareTo(BigDecimal.ZERO) < 0) {
//                        throw new InsufficientBalanceException(balance, amount);
//                    }
//                    account.setBalance(finalMoneyAmount);
//                    return finalMoneyAmount;
//                } else {
//                    throw new InsufficientBalanceException(balance, amount);
//                    //custom exception
//                }
//            }
//        }
//        return new BigDecimal("0.00"); //change this to error
//    }
//
//    public List<Account> getAccounts() {
//        return accounts;
//    }
//
//    public Account getAccount(String userid){
//        for(Account account : accounts){
//            if (account.getCustomer().equalsIgnoreCase(userid)){
//                return account;
//            }
//        }
//        return null;
//    }
//}
