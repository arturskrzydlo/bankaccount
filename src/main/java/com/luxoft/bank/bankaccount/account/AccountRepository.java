package com.luxoft.bank.bankaccount.account;

import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, Integer> {
}
