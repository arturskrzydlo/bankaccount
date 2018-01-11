package com.luxoft.bank.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override public double withdraw(Integer accountId, Double amountToWithdraw) {
        Account account = accountRepository.findOne(accountId);
        account.setBalance(account.getBalance() - amountToWithdraw);
        accountRepository.save(account);
        return account.getBalance();
    }

}
