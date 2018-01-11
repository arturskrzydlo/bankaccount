package com.luxoft.bank.bankaccount

import spock.lang.Specification

import java.lang.Void as Should

class WithdrawalDepositSpec extends Specification {

    def account = new Account()

    def accountId = 1
    def accountRepository = Mock(AccountRepository)
    def accountService = new AccountServiceImpl(accountRepository)

    Should "after withdrawal balance should be substracted with amount of withdrawal"() {

        given: "accont with #initialBalance balance"
            account.setBalance(initialBalance);

        and: "prepared result from db"
            def modifiedAccount = createSampleAccount()
            modifiedAccount.setBalance(expectedResult)

        when: "withdraw #withdrawal then amount should be #expectedResult"
            def result = accountService.withdraw(accountId, withdrawal)

        then:
            "returned balance is #expectedResult"
            result == expectedResult
        and:
            1 * accountRepository.save(account) >> modifiedAccount
            1 * accountRepository.findOne(_) >> account

        where:
            initialBalance | withdrawal || expectedResult
            200.0          | 100.0      || 100.0
            200.0          | 150.0      || 50.0
            0              | 0           | 0

    }

    def createSampleAccount() {
        def account = new Account()
        return account
    }
}
