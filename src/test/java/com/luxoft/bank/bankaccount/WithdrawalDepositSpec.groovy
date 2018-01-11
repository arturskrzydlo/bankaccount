package com.luxoft.bank.bankaccount

import com.luxoft.bank.bankaccount.account.*
import spock.lang.Specification

import java.lang.Void as Should

class WithdrawalDepositSpec extends Specification {

    def debitAccount = createSampleDebitAccount()
    def creditAccount = createSampleCreditAccount()

    def accountId = 1
    def accountRepository = Mock(AccountRepository)
    def accountService = new AccountServiceImpl(accountRepository)

    Should "after withdrawal balance should be substracted with amount of withdrawal"() {

        given: "accont with #initialBalance balance"
            debitAccount.setBalance(initialBalance);

        and: "prepared result from db"
            def modifiedAccount = createSampleDebitAccount()
            modifiedAccount.setBalance(expectedResult)

        when: "withdraw #withdrawal then amount should be #expectedResult"
            def result = accountService.withdraw(accountId, withdrawal)

        then:
            "returned balance is #expectedResult"
            result == expectedResult
        and:
            1 * accountRepository.save(debitAccount) >> modifiedAccount
            1 * accountRepository.findOne(_) >> debitAccount

        where:
            initialBalance | withdrawal || expectedResult
            200.0          | 100.0      || 100.0
            200.0          | 150.0      || 50.0
            0              | 0           | 0

    }

    Should "throw NoSufficientFundsException when withdrawal exceed  debit account balance "() {

        given: "withdrawal which exceedes initial balance (which is zero)"
            def withDrawalWhichExceedesBalance = 100
            accountRepository.findOne(accountId) >> debitAccount

        when:
            accountService.withdraw(accountId, withDrawalWhichExceedesBalance)

        then:
            NotSufficientFundsException exception = thrown()
            exception.message == "You exceedes your account balance for " + withDrawalWhichExceedesBalance
    }

    def createSampleDebitAccount() {
        def account = new Account()
        account.setAccountType(AccountType.DEBIT)
        return account
    }

    def createSampleCreditAccount() {
        def account = new Account()
        account.setAccountType(AccountType.CREDIT)
        return account
    }
}
