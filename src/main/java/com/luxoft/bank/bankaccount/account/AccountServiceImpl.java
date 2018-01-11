package com.luxoft.bank.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
            AccountOperationRepository accountOperationRepository) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }

    @Override public double withdraw(Integer accountId, Double amountToWithdraw) throws NotSufficientFundsException {
        Account account = accountRepository.findOne(accountId);

        if (!isEnoughFunds(account, amountToWithdraw)) {
            throw new NotSufficientFundsException(-(account.getBalance() - amountToWithdraw));
        }

        account.setBalance(account.getBalance() - amountToWithdraw);
        accountRepository.save(account);
        storeAccountOperation(account, AccountOperationType.WITHDRAWAL, amountToWithdraw);
        return account.getBalance();
    }

    @Override public double deposit(Integer accountId, Double amountToDeposit) {
        Account account = accountRepository.findOne(accountId);
        account.setBalance(account.getBalance() + amountToDeposit);
        accountRepository.save(account);
        storeAccountOperation(account, AccountOperationType.DEPOSIT, amountToDeposit);
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

        storeAccountOperation(sourceAccount, AccountOperationType.TRANSFER, amountToTransfer);
        storeAccountOperation(destinationAccount, AccountOperationType.TRANSFER, amountToTransfer);

        return sourceAccount.getBalance();

    }

    @Override public AccountStatement getAccountStatement(Integer accountId, YearMonth yearMonth)
            throws NoSuchAccountException {

        if (!accountRepository.exists(accountId)) {
            throw new NoSuchAccountException(accountId);
        }

        List<AccountOperation> accountOperations = accountOperationRepository.findByAccountIdAndOperationTime(accountId,
                yearMonth.getYear(), yearMonth.getMonthValue());

        return createAccountStatement(accountOperations, accountId, yearMonth);
    }

    private AccountStatement createAccountStatement(List<AccountOperation> accountOperations, Integer accountId,
            YearMonth yearMonth) {

        AccountStatement accountStatement = new AccountStatement();
        accountStatement.setAccountId(accountId);
        accountStatement.setStatementYearMonth(yearMonth);
        accountStatement.setStatementDate(LocalDateTime.now());
        accountStatement.setOperations(accountOperations);

        return accountStatement;
    }

    private boolean isEnoughFunds(Account account, Double amountToBeTakenFromAccount) {
        if (account.getAccountType().equals(AccountType.DEBIT)) {
            return account.getBalance() >= amountToBeTakenFromAccount;
        }
        return true;
    }

    private AccountOperation storeAccountOperation(Account account, AccountOperationType operationType, Double amount) {
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAccount(account);
        accountOperation.setAmount(amount);
        accountOperation.setBalance(account.getBalance());
        accountOperation.setOperationType(operationType);
        accountOperation.setOperationTime(LocalDateTime.now());

        return accountOperationRepository.save(accountOperation);
    }

}
