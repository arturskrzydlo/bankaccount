package com.luxoft.bank.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override public double withdraw(Integer accountId, Double amountToWithdraw) throws NotSufficientFundsException {
        Account account = accountRepository.findOne(accountId);

        if (!isEnoughFunds(account, amountToWithdraw)) {
            throw new NotSufficientFundsException(-(account.getBalance() - amountToWithdraw));
        }

        account.setBalance(account.getBalance() - amountToWithdraw);
        accountRepository.save(account);
        return account.getBalance();
    }

    private boolean isEnoughFunds(Account account, Double amountToBeTakenFromAccount) {
        if (account.getAccountType().equals(AccountType.DEBIT)) {
            return account.getBalance() >= amountToBeTakenFromAccount;
        }
        return true;
    }

}
