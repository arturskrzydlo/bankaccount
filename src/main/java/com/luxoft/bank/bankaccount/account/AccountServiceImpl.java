package com.luxoft.bank.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    @Override public double deposit(Integer accountId, Double amountToDeposit) {
        Account account = accountRepository.findOne(accountId);
        account.setBalance(account.getBalance() + amountToDeposit);
        accountRepository.save(account);
        return account.getBalance();
    }

    @Transactional
    @Override public double transfer(Integer sourceAccountId, Integer destinationAccountId, Double amountToTransfer)
            throws NotSufficientFundsException, NoSuchAccountException {

        Account sourceAccount = accountRepository.findOne(sourceAccountId);
        Account destinationAccount = accountRepository.findOne(destinationAccountId);

        if (sourceAccount == null) {
            throw new NoSuchAccountException(sourceAccountId);
        }

        if (destinationAccount == null) {
            throw new NoSuchAccountException(destinationAccountId);
        }

        if (!isEnoughFunds(sourceAccount, amountToTransfer)) {
            throw new NotSufficientFundsException(-(sourceAccount.getBalance() - amountToTransfer));
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amountToTransfer);

        destinationAccount.setBalance(destinationAccount.getBalance() + amountToTransfer);
        accountRepository.save(destinationAccount);
        accountRepository.save(sourceAccount);

        return sourceAccount.getBalance();

    }

    private boolean isEnoughFunds(Account account, Double amountToBeTakenFromAccount) {
        if (account.getAccountType().equals(AccountType.DEBIT)) {
            return account.getBalance() >= amountToBeTakenFromAccount;
        }
        return true;
    }

}
