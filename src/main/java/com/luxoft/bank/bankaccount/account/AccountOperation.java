package com.luxoft.bank.bankaccount.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter class AccountOperation extends AbstractDomainClass {

    @ManyToOne
    private Account account;
    @Enumerated(EnumType.STRING)
    private AccountOperationType operationType;
    private LocalDateTime operationTime;
    private Double amount;
    private Double balance;

}
