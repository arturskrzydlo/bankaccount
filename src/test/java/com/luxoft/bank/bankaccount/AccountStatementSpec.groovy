package com.luxoft.bank.bankaccount

import com.luxoft.bank.bankaccount.account.Account
import com.luxoft.bank.bankaccount.account.AccountServiceImpl
import spock.lang.Specification

import java.lang.Void as Should
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth
import java.util.stream.Collectors
import java.util.stream.Stream

class AccountStatementSpec extends Specification {


    def transferOperationJanuary
    def transferOperationJanuary2
    def account

    def accountRepository = Mock(AccountRepository)
    def accountService = new AccountServiceImpl(accountRepository)

    def setup() {
        createSampleOperationList()
    }


    Should "return account statement with all operation from this month after calling getAccountStatement"() {
        given: "account from which statement will be produced"
            def accountId = account.getId()
        and:
            def yearMonth = YearMonth.of(2018, Month.JANUARY)
        when: "call for currentAccountStatement"
            def accountStatement = accountService.getAccountStatement(accountId, yearMonth)
        then: "Account statement with two operation will be returned"
            accountStatement.getOperations().size() == 2
        and: "Account statement is statement for account with id #accountId"
            accountStatement.getAccountId() == accountId
        and: "Returned statement is from month #yearMonth"
            accountStatement.getStatementYearMonth() == yearMonth
        and: "method to find account operations was called only once"
            1 * accountOperationRepository.findByAccountIdAndOperationTime(accountId, yearMonth.getYear(), yearMonth.getMonthValue()) >> {
                List<AccountOperation> operations = Stream.of(transferOperationJanuary, transferOperationJanuary2).collect(Collectors.toList())
                return operations
            }
            1 * accountRepository.exists(accountId) >> true

    }


    def createSampleOperationList() {

        account = new Account()
        account.setId(1)

        transferOperationJanuary = new AccountOperation()
        transferOperationJanuary.setOperationTime(LocalDateTime.of(2018, Month.JANUARY, 1, 22, 30))
        transferOperationJanuary.setAmount(200)
        transferOperationJanuary.setOperationType(AccountOperationType.TRANSFER)
        transferOperationJanuary.setBalance(300)
        transferOperationJanuary.setAccount(account)

        transferOperationJanuary2 = new AccountOperation()
        transferOperationJanuary2.setOperationTime(LocalDateTime.of(2018, Month.JANUARY, 12, 12, 34))
        transferOperationJanuary2.setOperationType(AccountOperationType.TRANSFER)
        transferOperationJanuary2.setAmount(200)
        transferOperationJanuary2.setBalance(100)
        transferOperationJanuary2.setAccount(account)

    }

}
