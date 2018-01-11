package com.luxoft.bank.bankaccount.account;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Getter
@Setter class AccountStatement {

    private LocalDateTime statementDate;
    private YearMonth statementYearMonth;
    private List<AccountOperation> operations;
    private Integer accountId;

}
