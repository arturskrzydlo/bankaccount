package com.luxoft.bank.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class AccountFacade {

    private AccountService accountService;

    @Autowired
    public AccountFacade(AccountService accountService) {
        this.accountService = accountService;
    }

    public double withdraw(Integer accountId, double amountToWithdraw) throws NotSufficientFundsException {
        return accountService.withdraw(accountId, amountToWithdraw);
    }

    public double deposit(Integer accountId, double amountToDeposit) {
        return accountService.deposit(accountId, amountToDeposit);
    }

    public double transfer(Integer sourceAccountId, Integer destinationAccountId, double transferAmount)
            throws NoSuchAccountException, NotSufficientFundsException {
        return accountService.transfer(sourceAccountId, destinationAccountId, transferAmount);
    }

    public AccountStatement getAccountStatement(Integer accountId, YearMonth yearMonth) throws NoSuchAccountException {
        return accountService.getAccountStatement(accountId, yearMonth);
    }

}
