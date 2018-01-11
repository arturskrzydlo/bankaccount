package com.luxoft.bank.bankaccount.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface AccountOperationRepository extends CrudRepository<AccountOperation, Integer> {

    @Query("select e from AccountOperation e where account.id = ?1 and year(e.operationTime) = ?2 and month(e.operationTime) = ?3")
    List<AccountOperation> findByAccountIdAndOperationTime(int accountId, int year, int month);
}
