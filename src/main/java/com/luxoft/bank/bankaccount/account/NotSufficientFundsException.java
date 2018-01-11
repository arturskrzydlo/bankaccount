package com.luxoft.bank.bankaccount.account;

import java.text.MessageFormat;

public class NotSufficientFundsException extends Exception {

    private static final String MESSAGE = "You exceedes your account balance for {0}";
    double amountWhichExceedesBalance;

    public NotSufficientFundsException(double amountWhichExceedesBalance) {
        super(MessageFormat.format(MESSAGE, amountWhichExceedesBalance));
        this.amountWhichExceedesBalance = amountWhichExceedesBalance;
    }
}
