package com.luxoft.bank.bankaccount.account;

interface AccountService {

    double withdraw(Integer accountId, Double amountToWithdraw) throws NotSufficientFundsException;

    double deposit(Integer acccountId, Double amountToDeposit);

    double transfer(Integer sourceAccountId, Integer destinationAccountId, Double amountOfMoney)
            throws NotSufficientFundsException, NoSuchAccountException;

}
