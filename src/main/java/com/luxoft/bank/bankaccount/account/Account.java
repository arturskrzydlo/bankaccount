package com.luxoft.bank.bankaccount.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter class Account extends AbstractDomainClass {

    private static final double INITIAL_BALANCE = 0.0;

    private Double balance = INITIAL_BALANCE;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
