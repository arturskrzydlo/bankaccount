package com.luxoft.bank.bankaccount.account;

import java.text.MessageFormat;

class NoSuchAccountException extends Exception {

    private static final String MESSAGE = "Account with id {0} doesnt exists";
    int accountId;

    public NoSuchAccountException(int accountId) {
        super(MessageFormat.format(MESSAGE, accountId));
        this.accountId = accountId;
    }

}
